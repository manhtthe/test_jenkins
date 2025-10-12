package com.web.bookingKol.domain.user.rest;


import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.BookedPackageResponse;
import com.web.bookingKol.domain.user.services.BookingUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/user/bookings")
@RequiredArgsConstructor
public class BookingUserController {

    private final BookingUserService bookingUserService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<BookedPackageResponse>>> getUserBookings(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @RequestParam(required = false) String packageType,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(
                bookingUserService.getUserBookings(
                        userDetails.getUsername(),
                        search,
                        startDate,
                        endDate,
                        packageType,
                        pageable
                )
        );
    }
}

