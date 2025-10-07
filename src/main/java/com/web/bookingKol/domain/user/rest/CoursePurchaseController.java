package com.web.bookingKol.domain.user.rest;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.PurchasedCourseResponse;
import com.web.bookingKol.domain.user.services.CoursePurchaseService;
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
import java.time.LocalDate;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CoursePurchaseController {

    private final CoursePurchaseService coursePurchaseService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PagedResponse<PurchasedCourseResponse>>> getPurchaseHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @PageableDefault(size = 10, sort = "startDate") Pageable pageable
    ) {
        return ResponseEntity.ok(
                coursePurchaseService.getPurchaseHistory(userDetails.getUsername(), search, startDate, endDate, pageable)
        );
    }

}


