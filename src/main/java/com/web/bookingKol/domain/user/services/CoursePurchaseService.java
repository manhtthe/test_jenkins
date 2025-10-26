package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.PurchasedCourseResponse;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface CoursePurchaseService {
    ApiResponse<PagedResponse<PurchasedCourseResponse>> getPurchaseHistory(
            String userEmail,
            String search,
            Instant startDate,
            Instant endDate,
            Pageable pageable
    );
}


