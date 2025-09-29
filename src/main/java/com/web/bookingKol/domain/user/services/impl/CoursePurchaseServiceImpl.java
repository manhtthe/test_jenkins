package com.web.bookingKol.domain.user.services.impl;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.PurchasedCourseResponse;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.PurchasedCoursePackageRepository;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import com.web.bookingKol.domain.user.services.CoursePurchaseService;
import com.web.bookingKol.temp_models.PurchasedCoursePackage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursePurchaseServiceImpl implements CoursePurchaseService {

    private final UserRepository userRepository;
    private final PurchasedCoursePackageRepository purchasedCoursePackageRepository;

    @Override
    public ApiResponse<?> getPurchaseHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        List<PurchasedCoursePackage> purchases = purchasedCoursePackageRepository.findByUser(user);

        List<PurchasedCourseResponse> response = purchases.stream().map(p -> PurchasedCourseResponse.builder()
                        .id(p.getId())
                        .courseName(p.getCoursePackage().getName())
                        .currentPrice(p.getCurrentPrice())
                        .isPaid(p.getIsPaid())
                        .status(p.getStatus())
                        .startDate(p.getStartDate())
                        .endDate(p.getEndDate())
                        .build())
                .toList();

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Lấy lịch sử mua khóa học thành công"))
                .data(response)
                .build();
    }
}

