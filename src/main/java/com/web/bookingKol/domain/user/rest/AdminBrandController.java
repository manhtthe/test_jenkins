package com.web.bookingKol.domain.user.rest;

import com.web.bookingKol.domain.user.dtos.BrandUserSummaryResponse;
import com.web.bookingKol.domain.user.services.AdminBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
public class AdminBrandController {

    private final AdminBrandService adminBrandService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public Page<BrandUserSummaryResponse> getAllBrands(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return adminBrandService.getBrandUsers(search, pageable);
    }
}

