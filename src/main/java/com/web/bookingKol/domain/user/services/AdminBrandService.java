package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.domain.user.dtos.BrandUserSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminBrandService {
    Page<BrandUserSummaryResponse> getBrandUsers(String search, Pageable pageable);
}

