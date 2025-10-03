package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.domain.user.dtos.BrandUserSummaryResponse;
import com.web.bookingKol.domain.user.repositories.BrandRepository;
import com.web.bookingKol.domain.user.services.AdminBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminBrandServiceImpl implements AdminBrandService {

    private final BrandRepository brandRepository;

    @Override
    public Page<BrandUserSummaryResponse> getBrandUsers(String search, Pageable pageable) {
        return brandRepository.findBrandUsers(search, pageable);
    }
}

