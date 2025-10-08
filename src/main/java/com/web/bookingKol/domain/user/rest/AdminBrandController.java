package com.web.bookingKol.domain.user.rest;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.BrandUserSummaryResponse;
import com.web.bookingKol.domain.user.services.AdminBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
public class AdminBrandController {

    private final AdminBrandService adminBrandService;

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ApiResponse<PagedResponse<BrandUserSummaryResponse>> getAllBrands(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        Page<BrandUserSummaryResponse> pageResult = adminBrandService.getBrandUsers(search, pageable);
        return ApiResponse.<PagedResponse<BrandUserSummaryResponse>>builder()
                .status(200)
                .message(List.of("Lấy danh sách brand thành công"))
                .data(PagedResponse.fromPage(pageResult))
                .build();
    }
}

