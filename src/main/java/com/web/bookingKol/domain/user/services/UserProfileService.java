package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.UpdateProfileRequest;

import java.util.UUID;

public interface UserProfileService {
    ApiResponse<?> updateProfile(UpdateProfileRequest request);
    ApiResponse<?> getProfile();
    ApiResponse<?> getProfileByAdmin(UUID userId);
}

