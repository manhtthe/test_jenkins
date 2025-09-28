package com.web.bookingKol.domain.user.services.impl;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.common.services.EmailService;
import com.web.bookingKol.domain.user.models.PasswordResetToken;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.PasswordResetTokenRepository;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import com.web.bookingKol.domain.user.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public ApiResponse<?> sendOtp(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(List.of("Email không tồn tại"))
                    .data(null)
                    .build();
        }
        String otp = String.format("%06d", (int)(Math.random() * 1_000_000));

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(otp)
                .email(email)
                .expiresAt(Instant.now().plus(10, ChronoUnit.MINUTES))
                .used(false)
                .build();
        tokenRepository.save(resetToken);

        String subject = "Mã OTP đặt lại mật khẩu - Nexussocial";
        String content = "Xin chào!\n\nMã OTP để đặt lại mật khẩu của bạn là: "
                + otp + "\nMã này sẽ hết hạn sau 10 phút.";

        emailService.sendSimpleEmail(email, subject, content);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Đã gửi mã OTP về email của bạn"))
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<?> verifyOtpAndResetPassword(String email, String otp, String newPassword,String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("Mật khẩu xác nhận không khớp"))
                    .data(null)
                    .build();
        }
        PasswordResetToken resetToken = tokenRepository.findByEmailAndToken(email, otp)
                .orElseThrow(() -> new IllegalArgumentException("OTP không hợp lệ"));
        if (!resetToken.getEmail().equals(email)) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("Email không khớp với OTP"))
                    .data(null)
                    .build();
        }

        if (resetToken.isUsed()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("OTP đã được sử dụng"))
                    .data(null)
                    .build();
        }

        if (resetToken.getExpiresAt().isBefore(Instant.now())) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("OTP đã hết hạn"))
                    .data(null)
                    .build();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Đổi mật khẩu thành công"))
                .data(null)
                .build();
    }
}


