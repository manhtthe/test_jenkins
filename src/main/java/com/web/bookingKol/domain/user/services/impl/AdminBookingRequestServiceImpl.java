package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.booking.models.BookingPackageKol;
import com.web.bookingKol.domain.booking.models.BookingRequest;
import com.web.bookingKol.domain.booking.models.Campaign;
import com.web.bookingKol.domain.booking.models.Contract;
import com.web.bookingKol.domain.booking.repositories.BookingRequestRepository;
import com.web.bookingKol.domain.booking.repositories.ContractRepository;
import com.web.bookingKol.domain.user.dtos.AdminBookingRequestResponse;
import com.web.bookingKol.domain.user.dtos.AdminCreateBookingRequestDTO;
import com.web.bookingKol.domain.user.dtos.KolInfo;
import com.web.bookingKol.domain.user.dtos.UpdateBookingRequestStatusDTO;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.BookingPackageKolRepository;
import com.web.bookingKol.domain.user.repositories.CampaignRepository;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import com.web.bookingKol.domain.user.services.AdminBookingRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBookingRequestServiceImpl implements AdminBookingRequestService {

    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final BookingRequestRepository bookingRequestRepository;
    private final ContractRepository contractRepository;
    private final ContractGeneratorService contractGeneratorService;
    private final BookingPackageKolRepository bookingPackageKolRepository;

    @Override
    @Transactional
    public ApiResponse<?> createBookingRequest(AdminCreateBookingRequestDTO dto, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy admin: " + adminEmail));

        Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy campaign"));

        BookingRequest booking = new BookingRequest();
        booking.setId(UUID.randomUUID());
        booking.setCampaign(campaign);
        booking.setUser(campaign.getCreatedBy());
        booking.setDescription(dto.getDescription());
        booking.setStatus(dto.getStatus());
        booking.setRepeatType(dto.getRepeatType());
        booking.setDayOfWeek(dto.getDayOfWeek());
        booking.setRepeatUntil(dto.getRepeatUntil());
        booking.setCreatedAt(Instant.now());
        booking.setUpdatedAt(Instant.now());
        if (dto.getContractAmount() != null) {
            booking.setContractAmount(dto.getContractAmount());
        } else {
            booking.setContractAmount(BigDecimal.ZERO);
        }
        bookingRequestRepository.saveAndFlush(booking);

        String savedContractPath = null;
        MultipartFile contractFile = dto.getContractFile();

        if (contractFile != null && !contractFile.isEmpty()) {
            try {
                String uploadDir = "uploads/contracts/" + Instant.now().toEpochMilli();
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = UUID.randomUUID() + "_" + contractFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                Files.copy(contractFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                savedContractPath = filePath.toString();
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi lưu file hợp đồng", e);
            }
        } else {
            Contract contract = new Contract();
            contract.setBookingRequest(booking);
            contract.setContractNumber("CT-" + System.currentTimeMillis());
            contract.setStatus("DRAFT");
            contract.setCreatedAt(Instant.now());
            contract.setUpdatedAt(Instant.now());
            contractRepository.saveAndFlush(contract);

            var placeholders = Map.of(
                    "brand_name", campaign.getCreatedBy().getFullName(),
                    "kol_name", booking.getUser().getFullName(),
                    "campaign_name", campaign.getName(),
                    "today", LocalDate.now().toString(),
                    "contract_number", contract.getContractNumber(),
                    "contract_amount", booking.getContractAmount().toString()
            );

            var fileUsage = contractGeneratorService.generateAndSaveContract(placeholders, admin.getId(), contract.getId());

            contract.setTerms("File hợp đồng: " + fileUsage.getFile().getFileUrl());
            contractRepository.save(contract);

            savedContractPath = fileUsage.getFile().getFileUrl();
        }


        Contract contract = new Contract();
        contract.setBookingRequest(booking);
        contract.setContractNumber("CT-" + System.currentTimeMillis());
        contract.setStatus("DRAFT");
        contract.setTerms(savedContractPath != null
                ? "File hợp đồng: " + savedContractPath
                : "Chưa có file hợp đồng");
        contract.setCreatedAt(Instant.now());
        contract.setUpdatedAt(Instant.now());
        contractRepository.save(contract);


        final String savedContractPathFinal = savedContractPath;

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Tạo booking request và hợp đồng thành công"))
                .data(
                        new Object() {
                            public final UUID bookingRequestId = booking.getId();
                            public final UUID contractId = contract.getId();
                            public final String contractPath = savedContractPathFinal;
                            public final String contractStatus = contract.getStatus();
                        }
                )
                .build();
    }


    @Override
    public ApiResponse<PagedResponse<AdminBookingRequestResponse>> getAllBookingRequests(Pageable pageable) {

        Page<BookingRequest> page = bookingRequestRepository.findAll(pageable);

        Page<AdminBookingRequestResponse> mapped = page.map(br -> {
            var campaign = br.getCampaign();

            List<BookingPackageKol> links = bookingPackageKolRepository.findByPurchasedPackage_Campaign_Id(campaign.getId());
            List<KolInfo> kols = links.stream()
                    .filter(b -> "KOL".equalsIgnoreCase(b.getRoleInBooking()))
                    .map(b -> KolInfo.builder()
                            .id(b.getKol().getId())
                            .displayName(b.getKol().getDisplayName())
                            .build())
                    .collect(Collectors.toList());

            List<KolInfo> lives = links.stream()
                    .filter(b -> "LIVE".equalsIgnoreCase(b.getRoleInBooking()))
                    .map(b -> KolInfo.builder()
                            .id(b.getKol().getId())
                            .displayName(b.getKol().getDisplayName())
                            .build())
                    .collect(Collectors.toList());

            Contract contract = br.getContracts().stream()
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .findFirst()
                    .orElse(null);

            return AdminBookingRequestResponse.builder()
                    .bookingRequestId(br.getId())
                    .description(br.getDescription())
                    .status(br.getStatus())
                    .repeatType(br.getRepeatType())
                    .dayOfWeek(br.getDayOfWeek())
                    .repeatUntil(br.getRepeatUntil())
                    .contractAmount(br.getContractAmount())
                    .createdAt(br.getCreatedAt())
                    .updatedAt(br.getUpdatedAt())

                    .campaignId(campaign.getId())
                    .campaignName(campaign.getName())
                    .campaignObjective(campaign.getObjective())
                    .budgetMin(campaign.getBudgetMin())
                    .budgetMax(campaign.getBudgetMax())
                    .startDate(campaign.getStartDate())
                    .endDate(campaign.getEndDate())
                    .createdByEmail(campaign.getCreatedBy().getEmail())

                    .kols(kols)
                    .lives(lives)

                    .contractId(contract != null ? contract.getId() : null)
                    .contractNumber(contract != null ? contract.getContractNumber() : null)
                    .contractStatus(contract != null ? contract.getStatus() : null)
                    .contractTerms(contract != null ? contract.getTerms() : null)
                    .contractFileUrl(contract != null ? extractFileUrl(contract.getTerms()) : null)
                    .build();
        });

        return ApiResponse.<PagedResponse<AdminBookingRequestResponse>>builder()
                .status(200)
                .message(List.of("Lấy danh sách booking request thành công"))
                .data(PagedResponse.fromPage(mapped))
                .build();
    }

    private String extractFileUrl(String terms) {
        if (terms == null) return null;
        return terms.contains("uploads/") ? terms.substring(terms.indexOf("uploads/")).trim() : terms;
    }


    @Override
    @Transactional
    public ApiResponse<?> updateBookingRequestStatus(UUID id, UpdateBookingRequestStatusDTO dto, String adminEmail) {
        var admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy admin: " + adminEmail));

        var booking = bookingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking request: " + id));

        booking.setStatus(dto.getStatus());
        booking.setUpdatedAt(java.time.Instant.now());
        bookingRequestRepository.save(booking);

        return ApiResponse.builder()
                .status(org.springframework.http.HttpStatus.OK.value())
                .message(java.util.List.of("Cập nhật trạng thái booking request thành công"))
                .data(new Object() {
                    public final java.util.UUID bookingRequestId = booking.getId();
                    public final String newStatus = booking.getStatus();
                    public final String updatedBy = admin.getEmail();
                })
                .build();
    }

}

