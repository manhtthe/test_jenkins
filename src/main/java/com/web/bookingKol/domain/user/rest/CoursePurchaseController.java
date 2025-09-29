package com.web.bookingKol.domain.user.rest;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.services.CoursePurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CoursePurchaseController {

    private final CoursePurchaseService coursePurchaseService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN','SUPER_ADMIN')")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<?>> getPurchaseHistory(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(coursePurchaseService.getPurchaseHistory(userDetails.getUsername()));
    }
}

