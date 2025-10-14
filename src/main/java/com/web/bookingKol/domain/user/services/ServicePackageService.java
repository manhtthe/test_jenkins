package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.ServicePackageDTO;
import com.web.bookingKol.temp_models.ServicePackage;

import java.util.List;

public interface ServicePackageService {
    ApiResponse<List<ServicePackageDTO>> getAllPackages(String type);
}

