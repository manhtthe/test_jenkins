package com.web.bookingKol.domain.user.rest;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.ForgotPasswordRequest;
import com.web.bookingKol.domain.user.dtos.ResetPasswordRequest;
import com.web.bookingKol.domain.user.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class AuthPasswordController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(passwordResetService.sendOtp(request.getEmail()));
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(passwordResetService.verifyOtpAndResetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword(),
                request.getConfirmPassword()
        ));
    }

}

