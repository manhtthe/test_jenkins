package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.UpdateProfileRequest;
import com.web.bookingKol.domain.user.dtos.UserProfileResponse;
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
import java.util.UUID;

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

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getIntroduction() != null) user.setIntroduction(request.getIntroduction());
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);

        Brand brand = brandRepository.findByUser(user)
                .orElseGet(() -> {
                    Brand newBrand = new Brand();
                    newBrand.setUser(user);
                    return newBrand;
                });

        if (request.getBrandName() != null) brand.setBrandName(request.getBrandName());
        if (request.getDateOfBirth() != null) brand.setDateOfBirth(request.getDateOfBirth());
        if (request.getCountry() != null) brand.setCountry(request.getCountry());
        brandRepository.save(brand);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Cập nhật thông tin thành công"))
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<?> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Brand brand = brandRepository.findByUser(user).orElse(null);

        UserProfileResponse response = UserProfileResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .phone(user.getPhone())
                .address(user.getAddress())
                .introduction(user.getIntroduction())
                .brandName(brand != null ? brand.getBrandName() : null)
                .dateOfBirth(brand != null ? brand.getDateOfBirth() : null)
                .country(brand != null ? brand.getCountry() : null)
                .build();

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Lấy thông tin thành công"))
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<?> getProfileByAdmin(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        Brand brand = brandRepository.findByUser(user).orElse(null);

        UserProfileResponse response = UserProfileResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .phone(user.getPhone())
                .address(user.getAddress())
                .introduction(user.getIntroduction())
                .brandName(brand != null ? brand.getBrandName() : null)
                .dateOfBirth(brand != null ? brand.getDateOfBirth() : null)
                .country(brand != null ? brand.getCountry() : null)
                .build();

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Lấy thông tin profile thành công"))
                .data(response)
                .build();
    }

}


