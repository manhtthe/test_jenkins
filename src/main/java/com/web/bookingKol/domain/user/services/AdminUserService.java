package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;

import java.util.UUID;

public interface AdminUserService {
    ApiResponse<?> updateUserStatus(UUID userId, String status);
}

