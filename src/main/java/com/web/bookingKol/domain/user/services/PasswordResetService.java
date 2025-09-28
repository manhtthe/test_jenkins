package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;

public interface PasswordResetService {
    ApiResponse<?> sendOtp(String email);
    ApiResponse<?> verifyOtpAndResetPassword(String email, String otp, String newPassword, String confirmPassword);
}
