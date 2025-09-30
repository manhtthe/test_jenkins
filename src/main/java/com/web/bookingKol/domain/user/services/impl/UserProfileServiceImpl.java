package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.UpdateProfileRequest;
import com.web.bookingKol.domain.user.models.Brand;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.BrandRepository;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import com.web.bookingKol.domain.user.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final BrandRepository brandRepository;

    @Override
    public ApiResponse<?> updateProfile(UpdateProfileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        user.setFullName(request.getFullName());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setIntroduction(request.getIntroduction());
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);

        Brand brand = brandRepository.findByUser(user)
                .orElse(new Brand());
        brand.setUser(user);
        brand.setBrandName(request.getBrandName());
        brand.setDateOfBirth(request.getDateOfBirth());
        brand.setCountry(request.getCountry());
        brandRepository.save(brand);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Cập nhật thông tin thành công"))
                .data(null)
                .build();
    }
}

