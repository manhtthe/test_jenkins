package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.UpdateProfileRequest;

public interface UserProfileService {
    ApiResponse<?> updateProfile(UpdateProfileRequest request);
}

