package com.web.bookingKol.domain.kol.services.impl;

import com.web.bookingKol.common.Enums;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.file.FileService;
import com.web.bookingKol.domain.file.dtos.FileDTO;
import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import com.web.bookingKol.domain.kol.dtos.FilterKolDTO;
import com.web.bookingKol.domain.kol.dtos.KolDetailDTO;
import com.web.bookingKol.domain.kol.dtos.KolProfileDTO;
import com.web.bookingKol.domain.kol.dtos.NewKolDTO;
import com.web.bookingKol.domain.kol.mappers.KolDetailMapper;
import com.web.bookingKol.domain.kol.mappers.KolProfileMapper;
import com.web.bookingKol.domain.kol.models.Category;
import com.web.bookingKol.domain.kol.models.KolProfile;
import com.web.bookingKol.domain.kol.repositories.CategoryRepository;
import com.web.bookingKol.domain.kol.repositories.KolProfileRepository;
import com.web.bookingKol.domain.kol.services.KolProfileService;
import com.web.bookingKol.domain.user.models.Role;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.RoleRepository;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KolProfileServiceImpl implements KolProfileService {
    @Autowired
    private KolProfileRepository kolProfileRepository;
    @Autowired
    private KolProfileMapper kolProfileMapper;
    @Autowired
    private KolDetailMapper kolProfileDetailMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileService fileService;

    @Override
    public ApiResponse<KolDetailDTO> getKolProfileByUserId(UUID userId) {
        KolProfile kolProfile = kolProfileRepository.findByUserId(userId, Enums.TargetType.PORTFOLIO.name())
                .orElseThrow(() -> new EntityNotFoundException("KolProfile not found for userId: " + userId));
        return ApiResponse.<KolDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get kol profile success"))
                .data(kolProfileDetailMapper.toDto(kolProfile))
                .build();
    }

    @Override
    public ApiResponse<KolDetailDTO> getKolProfileByKolId(UUID kolId) {
        KolProfile kolProfile = kolProfileRepository.findByKolId(kolId, Enums.TargetType.PORTFOLIO.name())
                .orElseThrow(() -> new EntityNotFoundException("KolProfile not found for kolId: " + kolId));
        return ApiResponse.<KolDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get kol profile by kolId success"))
                .data(kolProfileDetailMapper.toDto(kolProfile))
                .build();
    }

    /**
     * Retrieve all KOL profiles from the repository.
     * Each KOL profile will contain cover files that are active and marked as cover.
     *
     * @return ApiResponse containing the list of all KOL profiles
     */
    @Override
    public ApiResponse<List<KolProfileDTO>> getAllKol() {
        List<KolProfileDTO> KolProfileDTOS = kolProfileRepository.findAll()
                .stream().map(kol -> {
                    KolProfileDTO dto = kolProfileMapper.toDto(kol);
                    dto.setFileUsageDtos(getActiveCoverFiles(dto.getFileUsageDtos()));
                    return dto;
                }).toList();
        return ApiResponse.<List<KolProfileDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get all kol profiles success"))
                .data(KolProfileDTOS)
                .build();
    }

    /**
     * Retrieve all available KOL profiles that have ACTIVE status.
     * Each KOL profile will contain cover files that are active and marked as cover.
     *
     * @return ApiResponse containing the list of available KOL profiles
     */
    @Override
    public ApiResponse<List<KolProfileDTO>> getAllKolAvailable() {
        List<KolProfileDTO> KolProfileDTOS = kolProfileRepository
                .findAllKolAvailable(Enums.UserStatus.ACTIVE.name())
                .stream().map(kol -> {
                    KolProfileDTO dto = kolProfileMapper.toDto(kol);
                    dto.setFileUsageDtos(getActiveCoverFiles(dto.getFileUsageDtos()));
                    return dto;
                }).toList();
        return ApiResponse.<List<KolProfileDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get all kol profiles success"))
                .data(KolProfileDTOS)
                .build();
    }

    /**
     * Retrieve all KOL profiles that belong to a specific category.
     * Each KOL profile will contain cover files that are active and marked as cover.
     *
     * @param categoryId the ID of the category to filter KOLs
     * @return ApiResponse containing the list of KOL profiles under the given category
     */
    @Override
    public ApiResponse<List<KolProfileDTO>> getAllKolProfilesByCategoryId(UUID categoryId) {
        List<KolProfileDTO> KolProfileDTOS = kolProfileRepository.findByCategoryId(categoryId)
                .stream().map(kol -> {
                    KolProfileDTO dto = kolProfileMapper.toDto(kol);
                    dto.setFileUsageDtos(getActiveCoverFiles(dto.getFileUsageDtos()));
                    return dto;
                }).toList();
        return ApiResponse.<List<KolProfileDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get all kol by Category success!"))
                .data(KolProfileDTOS)
                .build();
    }

    /**
     * Retrieve KOL profiles based on multiple filters such as rating, category, price, and city.
     * Only active KOLs will be returned.
     * Each KOL profile will contain cover files that are active and marked as cover.
     *
     * @param filterKolDTO the filter criteria for searching KOLs
     * @return ApiResponse containing the list of KOL profiles that match the filters,
     * or NOT_FOUND status if none are found
     */
    @Override
    public ApiResponse<List<KolProfileDTO>> getAllKolWithFilter(FilterKolDTO filterKolDTO) {
        Double minRating = filterKolDTO.getMinRating() != null ? filterKolDTO.getMinRating() : null;
        UUID categoryId = filterKolDTO.getCategoryId() != null ? filterKolDTO.getCategoryId() : null;
        Double minPrice = filterKolDTO.getMinBookingPrice() != null ? filterKolDTO.getMinBookingPrice() : null;
        String city = filterKolDTO.getCity() != null ? filterKolDTO.getCity().trim() : null;
        List<KolProfile> kolProfiles = kolProfileRepository
                .filterKols(minRating, categoryId, minPrice, city, Enums.UserStatus.ACTIVE.name());
        if (kolProfiles.isEmpty()) {
            return ApiResponse.<List<KolProfileDTO>>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(List.of("No kol profiles found with the given filters"))
                    .data(null)
                    .build();
        }
        List<KolProfileDTO> kolProfileDTOS = kolProfiles.stream().map(kol -> {
            KolProfileDTO dto = kolProfileMapper.toDto(kol);
            dto.setFileUsageDtos(getActiveCoverFiles(dto.getFileUsageDtos()));
            return dto;
        }).toList();
        return ApiResponse.<List<KolProfileDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get all kol profiles success"))
                .data(kolProfileDTOS)
                .build();
    }

    /**
     * Filter and return only active cover files from the given set of FileUsageDTOs.
     * A file is considered valid if:
     * - isCover = true
     * - file is not null
     * - file status = ACTIVE
     *
     * @param fileUsageDTOs the set of FileUsageDTOs to filter
     * @return a set of only active cover FileUsageDTOs, or empty set if none
     */
    private Set<FileUsageDTO> getActiveCoverFiles(Set<FileUsageDTO> fileUsageDTOs) {
        if (fileUsageDTOs == null) return Collections.emptySet();
        return fileUsageDTOs.stream()
                .filter(fu -> Boolean.TRUE.equals(fu.getIsCover())
                        && fu.getFile() != null
                        && Enums.FileStatus.ACTIVE.name().equals(fu.getFile().getStatus()))
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public ApiResponse<KolDetailDTO> createNewKolAccount(UUID AdminId, NewKolDTO newKolDTO, MultipartFile file) {
        User admin = userRepository.findById(AdminId)
                .orElseThrow(() -> new AccessDeniedException("Only admin can create new Kol accounts"));
        Role role = roleRepository.findByKey(Enums.Roles.KOL.name())
                .orElseThrow(() -> new RuntimeException("Role USER not found !!"));
        //Create new user account first
        User newKol = new User();
        newKol.setId(UUID.randomUUID());
        newKol.setEmail(newKolDTO.getEmail());
        newKol.setPhone(newKolDTO.getPhone());
        newKol.setFullName(newKolDTO.getFullName());
        newKol.setPasswordHash(passwordEncoder.encode(newKolDTO.getPassword()));
        newKol.setGender(newKolDTO.getGender().trim().equalsIgnoreCase(Enums.UserGender.Male.name()) ?
                Enums.UserGender.Male.name() : Enums.UserGender.Female.name());
        newKol.setRoles(Set.of(role));
        newKol.setAddress(newKolDTO.getAddress());
        if (file != null && !file.isEmpty()) {
            FileDTO fileDTO = fileService.uploadFilePoint(admin.getId(), file);
            newKol.setAvatarUrl(fileDTO.getFileUrl());
        }
        newKol.setStatus(Enums.UserStatus.ACTIVE.name());
        newKol.setCreatedAt(Instant.now());
        userRepository.save(newKol);
        //Create kol profile after user account is created
        Set<Category> categories = newKolDTO.getCategoryIds().stream()
                .map(catId -> categoryRepository.findById(catId)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + catId)))
                .collect(Collectors.toSet());
        KolProfile kolProfile = new KolProfile();
        kolProfile.setUser(newKol);
        kolProfile.setId(UUID.randomUUID());
        kolProfile.setCountry(newKolDTO.getCountry());
        kolProfile.setDisplayName(newKolDTO.getDisplayName());
        kolProfile.setExperience(newKolDTO.getExperience());
        kolProfile.setLanguages(newKolDTO.getLanguages());
        kolProfile.setRateCardNote(newKolDTO.getRateCardNote());
        kolProfile.setMinBookingPrice(newKolDTO.getMinBookingPrice());
        kolProfile.setIsAvailable(true);
        kolProfile.setCreatedAt(Instant.now());
        kolProfile.setCategories(categories);
        kolProfileRepository.save(kolProfile);
        return ApiResponse.<KolDetailDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(List.of("Create new kol account successfully"))
                .data(kolProfileDetailMapper.toDto(kolProfile))
                .build();
    }
}
