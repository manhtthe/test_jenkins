package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface CoursePurchaseService {
    ApiResponse<?> getPurchaseHistory(String userEmail, String search, Instant startDate, Instant endDate, Pageable pageable);
}


