package com.web.bookingKol.domain.user.services.impl;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.ServicePackageDTO;
import com.web.bookingKol.domain.user.repositories.ServicePackageRepository;
import com.web.bookingKol.domain.user.services.ServicePackageService;
import com.web.bookingKol.temp_models.ServicePackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicePackageServiceImpl implements ServicePackageService {

    private final ServicePackageRepository repository;

    @Override
    public ApiResponse<List<ServicePackageDTO>> getAllPackages(String type) {
        List<ServicePackage> packages;

        if (type == null || type.isBlank()) {
            packages = repository.findAll();
        } else {
            packages = repository.findByPackageTypeIgnoreCase(type);
        }

        List<ServicePackageDTO> dtoList = packages.stream()
                .map(pkg -> ServicePackageDTO.builder()
                        .id(pkg.getId())
                        .name(pkg.getName())
                        .description(pkg.getDescription())
                        .packageType(pkg.getPackageType())
                        .allowKolSelection(pkg.getAllowKolSelection())
                        .build())
                .collect(Collectors.toList());

        return ApiResponse.<List<ServicePackageDTO>>builder()
                .status(200)
                .message(List.of("Lấy danh sách gói thành công"))
                .data(dtoList)
                .build();
    }

}

