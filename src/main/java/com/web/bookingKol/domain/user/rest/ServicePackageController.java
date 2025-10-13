package com.web.bookingKol.domain.user.rest;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.services.impl.ServicePackageServiceImpl;
import com.web.bookingKol.temp_models.ServicePackage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicepackages")
@RequiredArgsConstructor
public class ServicePackageController {

    private final ServicePackageServiceImpl servicePackageService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServicePackage>>> getAllPackages(
            @RequestParam(required = false) String type
    ) {
        return ResponseEntity.ok(servicePackageService.getAllPackages(type));
    }
}

