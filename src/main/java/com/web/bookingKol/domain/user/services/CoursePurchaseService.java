package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;

public interface CoursePurchaseService {
    ApiResponse<?> getPurchaseHistory(String userEmail);
}

