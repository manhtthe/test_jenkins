package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.BookedPackageResponse;
import com.web.bookingKol.domain.user.dtos.UpdateBookingStatusRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface BookingAdminService {
    ApiResponse<PagedResponse<BookedPackageResponse>> getAllBookings(
            String search,
            Instant startDate,
            Instant endDate,
            String packageType,
            Pageable pageable
    );

    ApiResponse<?> updateBookingStatus(UUID bookingId, UpdateBookingStatusRequest request);
}

