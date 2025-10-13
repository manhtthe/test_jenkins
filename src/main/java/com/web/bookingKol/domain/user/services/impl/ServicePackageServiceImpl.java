package com.web.bookingKol.domain.user.services.impl;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.repositories.ServicePackageRepository;
import com.web.bookingKol.domain.user.services.ServicePackageService;
import com.web.bookingKol.temp_models.ServicePackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePackageServiceImpl implements ServicePackageService {

    private final ServicePackageRepository repository;

    @Override
    public ApiResponse<List<ServicePackage>> getAllPackages(String type) {
        List<ServicePackage> packages;

        if (type == null || type.isBlank()) {
            packages = repository.findAll();
        } else {
            packages = repository.findByPackageTypeIgnoreCase(type);
        }

        return ApiResponse.<List<ServicePackage>>builder()
                .status(200)
                .message(List.of("Lấy danh sách gói thành công"))
                .data(packages)
                .build();
    }
}

