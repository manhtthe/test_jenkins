package com.web.bookingKol.domain.user.rest;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.BookedPackageResponse;
import com.web.bookingKol.domain.user.dtos.UpdateBookingStatusRequest;
import com.web.bookingKol.domain.user.services.BookingAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class BookingAdminController {

    private final BookingAdminService bookingAdminService;

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<BookedPackageResponse>>> getAllBookings(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @RequestParam(required = false) String packageType,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(bookingAdminService.getAllBookings(search, startDate, endDate,packageType, pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity<ApiResponse<?>> updateBookingStatus(
            @PathVariable UUID id,
            @RequestBody UpdateBookingStatusRequest request
    ) {
        return ResponseEntity.ok(bookingAdminService.updateBookingStatus(id, request));
    }
}

