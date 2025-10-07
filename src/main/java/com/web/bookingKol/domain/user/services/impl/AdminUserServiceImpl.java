package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.common.Enums.UserStatus;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import com.web.bookingKol.domain.user.services.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    public ApiResponse<?> updateUserStatus(UUID userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        if (!status.equalsIgnoreCase(UserStatus.ACTIVE.name())
                && !status.equalsIgnoreCase(UserStatus.SUSPENDED.name())) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("Trạng thái chỉ có thể là ACTIVE hoặc SUSPENDED"))
                    .build();
        }

        UserStatus newStatus = UserStatus.valueOf(status.toUpperCase());
        user.setStatus(newStatus.name());
        userRepository.save(user);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Cập nhật trạng thái người dùng thành công"))
                .data(user.getStatus())
                .build();
    }
}

