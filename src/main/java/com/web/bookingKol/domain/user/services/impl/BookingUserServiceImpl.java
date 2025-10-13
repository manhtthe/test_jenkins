package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.booking.models.BookingPackageKol;
import com.web.bookingKol.domain.user.dtos.BookedPackageResponse;
import com.web.bookingKol.domain.user.dtos.KolInfo;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.BookingPackageKolRepository;
import com.web.bookingKol.domain.user.repositories.PurchasedServicePackageRepository;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import com.web.bookingKol.domain.user.services.BookingUserService;
import com.web.bookingKol.temp_models.PurchasedServicePackage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingUserServiceImpl implements BookingUserService {

    private final UserRepository userRepository;
    private final PurchasedServicePackageRepository purchasedRepo;
    private final BookingPackageKolRepository bookingPackageKolRepository;

    @Override
    public ApiResponse<PagedResponse<BookedPackageResponse>> getUserBookings(
            String userEmail,
            String search,
            Instant startDate,
            Instant endDate,
            String packageType,
            Pageable pageable
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Specification<PurchasedServicePackage> spec = (root, query, cb) ->
                cb.equal(root.get("campaign").get("createdBy"), user);

        if (search != null && !search.isBlank()) {
            String like = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("campaign").get("name")), like),
                    cb.like(cb.lower(root.get("packageField").get("name")), like),
                    cb.like(cb.lower(root.get("status")), like)
            ));
        }

        if (packageType != null && !packageType.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("packageField").get("packageType")),
                            packageType.toLowerCase())
            );
        }

        if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
        }

        Page<PurchasedServicePackage> page = purchasedRepo.findAll(spec, pageable);

        Page<BookedPackageResponse> result = page.map(p -> {
            List<BookingPackageKol> links = bookingPackageKolRepository.findByPurchasedPackageId(p.getId());

            List<KolInfo> kols = links.stream()
                    .filter(l -> "KOL".equalsIgnoreCase(l.getRoleInBooking()))
                    .map(l -> KolInfo.builder()
                            .id(l.getKol().getId())
                            .displayName(l.getKol().getDisplayName())
                            .build())
                    .collect(Collectors.toList());

            List<KolInfo> lives = links.stream()
                    .filter(l -> "LIVE".equalsIgnoreCase(l.getRoleInBooking()))
                    .map(l -> KolInfo.builder()
                            .id(l.getKol().getId())
                            .displayName(l.getKol().getDisplayName())
                            .build())
                    .collect(Collectors.toList());

            return BookedPackageResponse.builder()
                    .id(p.getId())
                    .campaignName(p.getCampaign() != null ? p.getCampaign().getName() : null)
                    .objective(p.getCampaign() != null ? p.getCampaign().getObjective() : null)
                    .budgetMin(p.getCampaign() != null ? p.getCampaign().getBudgetMin() : null)
                    .budgetMax(p.getCampaign() != null ? p.getCampaign().getBudgetMax() : null)
                    .startDate(p.getCampaign() != null ? p.getCampaign().getStartDate() : null)
                    .endDate(p.getCampaign() != null ? p.getCampaign().getEndDate() : null)
                    .recurrencePattern(p.getRecurrencePattern())

                    .packageName(p.getPackageField() != null ? p.getPackageField().getName() : null)
                    .packageType(p.getPackageField() != null ? p.getPackageField().getPackageType() : null)
                    .price(p.getPrice() != null ? p.getPrice().doubleValue() : null)
                    .status(p.getStatus())

                    .buyerEmail(p.getCampaign() != null && p.getCampaign().getCreatedBy() != null
                            ? p.getCampaign().getCreatedBy().getEmail()
                            : null)

                    .kols(kols)
                    .lives(lives)
                    .createdAt(p.getCreatedAt())
                    .updatedAt(p.getUpdatedAt())
                    .build();
        });

        return ApiResponse.<PagedResponse<BookedPackageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Lấy lịch sử booking thành công"))
                .data(PagedResponse.fromPage(result))
                .build();
    }
}


