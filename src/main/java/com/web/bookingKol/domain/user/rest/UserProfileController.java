package com.web.bookingKol.domain.user.rest;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.UpdateProfileRequest;
import com.web.bookingKol.domain.user.services.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userProfileService.updateProfile(request));
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getProfile() {
        return ResponseEntity.ok(userProfileService.getProfile());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/admin/{userId}")
    public ResponseEntity<ApiResponse<?>> getProfileByAdmin(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(userProfileService.getProfileByAdmin(userId));
    }
}


