package com.web.bookingKol.domain.kol.services.impl;

import com.web.bookingKol.common.Enums;
import com.web.bookingKol.common.exception.UserAlreadyExistsException;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.file.FileService;
import com.web.bookingKol.domain.file.dtos.FileDTO;
import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import com.web.bookingKol.domain.file.mappers.FileMapper;
import com.web.bookingKol.domain.file.mappers.FileUsageMapper;
import com.web.bookingKol.domain.file.models.FileUsage;
import com.web.bookingKol.domain.file.repositories.FileUsageRepository;
import com.web.bookingKol.domain.kol.dtos.*;
import com.web.bookingKol.domain.kol.mappers.KolCreatedMapper;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KolProfileServiceImpl implements KolProfileService {
    @Autowired
    private KolProfileRepository kolProfileRepository;
    @Autowired
    private KolProfileMapper kolProfileMapper;
    @Autowired
    private KolDetailMapper kolDetailMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private KolCreatedMapper kolCreatedMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileUsageRepository fileUsageRepository;
    @Autowired
    private FileUsageMapper fileUsageMapper;

    @Override
    public ApiResponse<KolDetailDTO> getKolProfileByUserId(UUID userId) {
        KolProfile kolProfile = kolProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("KolProfile not found for userId: " + userId));
        return ApiResponse.<KolDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get kol profile success"))
                .data(kolDetailMapper.toDto(kolProfile))
                .build();
    }

    @Override
    public ApiResponse<KolDetailDTO> getKolProfileByKolId(UUID kolId) {
        KolProfile kolProfile = kolProfileRepository.findByKolId(kolId)
                .orElseThrow(() -> new EntityNotFoundException("KolProfile not found for kolId: " + kolId));
        return ApiResponse.<KolDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get kol profile by kolId success"))
                .data(kolDetailMapper.toDto(kolProfile))
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

    /**
     * Create a new KOL account along with their profile.
     * Only an admin user can perform this operation.
     * If an avatar file is provided, it will be uploaded and linked to the KOL profile.
     *
     * @param AdminId    the ID of the admin user creating the KOL
     * @param newKolDTO  the details of the new KOL to create
     * @param fileAvatar (optional) avatar image file for the KOL
     * @return ApiResponse containing the created KOL details
     * @throws AccessDeniedException      if the user is not an admin
     * @throws UserAlreadyExistsException if the email already exists
     * @throws IllegalArgumentException   if required roles or categories are not found
     */
    @Transactional
    @Override
    public ApiResponse<KolCreatedDTO> createNewKolAccount(UUID AdminId, NewKolDTO newKolDTO, MultipartFile fileAvatar) {
        User admin = userRepository.findById(AdminId)
                .orElseThrow(() -> new AccessDeniedException("Only admin can create new Kol accounts"));
        Role role = roleRepository.findByKey(Enums.Roles.KOL.name())
                .orElseThrow(() -> new IllegalArgumentException("Role KOL not found !!"));
        //Create user account for kol
        User newKol = new User();
        if (userRepository.existsByEmail(newKolDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + newKolDTO.getEmail());
        }
        newKol.setEmail(newKolDTO.getEmail());
        newKol.setPhone(newKolDTO.getPhone());
        newKol.setFullName(newKolDTO.getFullName());
        newKol.setPasswordHash(passwordEncoder.encode(newKolDTO.getPassword()));
        if (newKolDTO.getGender() != null) {
            newKol.setGender(newKolDTO.getGender().trim().equalsIgnoreCase(Enums.UserGender.Male.name()) ?
                    Enums.UserGender.Male.name() : Enums.UserGender.Female.name());
        }
        newKol.setRoles(new LinkedHashSet<>(Collections.singletonList(role)));
        newKol.setAddress(newKolDTO.getAddress());
        newKol.setStatus(Enums.UserStatus.ACTIVE.name());
        newKol.setCreatedAt(Instant.now());
        newKol = userRepository.saveAndFlush(newKol);
        //Create kol profile after user account is created
        KolProfile kolProfile = new KolProfile();
        kolProfile.setUser(newKol);
        kolProfile.setId(UUID.randomUUID());
        kolProfile.setDisplayName(newKolDTO.getDisplayName());
        kolProfile.setBio(newKolDTO.getBio());
        kolProfile.setCountry(newKolDTO.getCountry());
        kolProfile.setCity(newKolDTO.getCity());
        kolProfile.setLanguages(newKolDTO.getLanguages());
        kolProfile.setRateCardNote(newKolDTO.getRateCardNote());
        kolProfile.setMinBookingPrice(newKolDTO.getMinBookingPrice());
        kolProfile.setIsAvailable(true);
        kolProfile.setCreatedAt(Instant.now());
        kolProfile.setDob(newKolDTO.getDob());
        kolProfile.setExperience(newKolDTO.getExperience());
        if (newKolDTO.getCategoryIds() != null && !newKolDTO.getCategoryIds().isEmpty()) {
            Set<Category> categories = newKolDTO.getCategoryIds().stream()
                    .map(catId -> categoryRepository.findById(catId)
                            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + catId)))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            kolProfile.setCategories(categories);
        }
        kolProfileRepository.save(kolProfile);
        if (fileAvatar != null && !fileAvatar.isEmpty()) {
            FileDTO fileDTO = fileService.uploadFilePoint(admin.getId(), fileAvatar);
            newKol.setAvatarUrl(fileDTO.getFileUrl());
            // Link avatar file to kol profile
            fileService.createFileUsage(fileMapper.toEntity(fileDTO), kolProfile.getId(), Enums.TargetType.AVATAR.name(), false);
            userRepository.save(newKol);
        }
        return ApiResponse.<KolCreatedDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(List.of("Create new kol account successfully"))
                .data(kolCreatedMapper.toDto(kolProfile, kolProfile.getUser()))
                .build();
    }

    /**
     * Update an existing KOL profile and their associated user information.
     * Only an admin user can perform this operation.
     * If a new avatar file is provided, it will be uploaded and linked to the KOL profile.
     *
     * @param AdminId      the ID of the admin user performing the update
     * @param kolId        the ID of the KOL profile to update
     * @param updateKolDTO the updated details for the KOL
     * @param fileAvatar   (optional) upload new avatar image file for the KOL
     * @return ApiResponse containing the updated KOL details
     * @throws AccessDeniedException   if the user is not an admin
     * @throws EntityNotFoundException if the KOL profile is not found
     */
    @Transactional
    @Override
    public ApiResponse<KolDetailDTO> updateKolProfile(UUID AdminId, UUID kolId, UpdateKolDTO updateKolDTO, MultipartFile fileAvatar) {
        User admin = userRepository.findById(AdminId)
                .orElseThrow(() -> new AccessDeniedException("Only admin can create new Kol accounts"));
        KolProfile kolProfile = kolProfileRepository.findById(kolId)
                .orElseThrow(() -> new EntityNotFoundException("KolProfile not found for Kol Id: " + kolId));
        User kolUser = kolProfile.getUser();
        // Update kol user info and kol profile info
        if (updateKolDTO != null) {
            BeanUtils.copyProperties(updateKolDTO, kolUser, getNullPropertyNames(updateKolDTO));
            BeanUtils.copyProperties(updateKolDTO, kolProfile, getNullPropertyNames(updateKolDTO));
        }
        if (fileAvatar != null && !fileAvatar.isEmpty()) {
            FileDTO fileDTO = fileService.uploadFilePoint(admin.getId(), fileAvatar);
            kolUser.setAvatarUrl(fileDTO.getFileUrl());
            // Link avatar file to kol profile
            fileService.createFileUsage(fileMapper.toEntity(fileDTO), kolProfile.getId(), Enums.TargetType.AVATAR.name(), false);
        }
        kolProfile.setUpdatedAt(Instant.now());
        userRepository.save(kolUser);
        kolProfileRepository.save(kolProfile);
        return ApiResponse.<KolDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Update kol profile successfully"))
                .data(kolDetailMapper.toDto(kolProfile))
                .build();
    }

    /**
     * Utility method to get names of properties with null values in the source object.
     * This is used to ignore null properties during BeanUtils.copyProperties.
     *
     * @param source the source object to check for null properties
     * @return an array of property names that have null values
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Transactional
    @Override
    public ApiResponse<List<FileUsageDTO>> uploadKolImagePortfolio(UUID uploaderId, UUID kolId, List<MultipartFile> files) {
        if (!userRepository.existsById(uploaderId)) {
            throw new EntityNotFoundException("Uploader not found: " + uploaderId);
        }
        if (!kolProfileRepository.existsById(kolId)) {
            throw new EntityNotFoundException("KolProfile not found for Kol Id: " + kolId);
        }
        if (files != null && !files.isEmpty()) {
            List<FileUsageDTO> fileUsageDTOS = new ArrayList<>();
            for (MultipartFile file : files) {
                FileDTO fileDTO = fileService.uploadFilePoint(uploaderId, file);
                FileUsageDTO fileUsageDTO = fileService.createFileUsage(fileMapper.toEntity(fileDTO), kolId, Enums.TargetType.PORTFOLIO.name(), false);
                fileUsageDTOS.add(fileUsageDTO);
            }
            return ApiResponse.<List<FileUsageDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message(List.of("Upload media files success: " + fileUsageDTOS.size() + " files"))
                    .data(fileUsageDTOS)
                    .build();
        } else {
            return ApiResponse.<List<FileUsageDTO>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("No files to upload"))
                    .data(null)
                    .build();
        }
    }

    @Override
    public ApiResponse<List<FileUsageDTO>> getAllKolMediaFiles(UUID kolId) {
        if (!kolProfileRepository.existsById(kolId)) {
            throw new EntityNotFoundException("KolProfile not found for Kol Id: " + kolId);
        }
        List<FileUsageDTO> kolPortfolios = fileUsageMapper.toDtoList(fileUsageRepository.findAllImageAndVideoActiveByKolId(kolId));
        if (kolPortfolios == null) {
            return ApiResponse.<List<FileUsageDTO>>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(List.of("No files found"))
                    .data(null)
                    .build();
        }
        return ApiResponse.<List<FileUsageDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get kol portfolios and avatars success: " + kolPortfolios.size() + " files"))
                .data(kolPortfolios)
                .build();
    }

    @Override
    public ApiResponse<?> activateOrDeactivateKolMediaFile(UUID kolId, List<UUID> fileUsageIds, boolean isActive) {
        if (!kolProfileRepository.existsById(kolId)) {
            throw new EntityNotFoundException("KolProfile not found for Kol Id: " + kolId);
        }
        List<FileUsage> fileUsages = fileUsageRepository.findAllById(fileUsageIds);
        if (fileUsages.size() != fileUsageIds.size()) {
            throw new EntityNotFoundException("Some FileUsages not found for provided IDs");
        }
        fileUsages.forEach(fileUsage -> fileUsage.setIsActive(isActive));
        fileUsageRepository.saveAll(fileUsages);
        String action = isActive ? "activated" : "deactivated";
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Successfully " + action + " " + fileUsages.size() + " media files"))
                .data(null)
                .build();
    }
}
