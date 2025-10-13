package com.web.bookingKol.domain.user.rest;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.AiConsultationLogResponse;
import com.web.bookingKol.domain.user.services.AiConsultationLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "GEMINI ADMIN")
@RequestMapping("/adminconsultation")
@RequiredArgsConstructor
public class GeminiAdminController {

    private final AiConsultationLogService logService;

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("/logs")
    public ApiResponse<PagedResponse<AiConsultationLogResponse>> getLogs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort.Direction dir = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortParams[0]));

        Page<AiConsultationLogResponse> logs = logService.getAllLogs(search, startDate, endDate, pageable);
        return ApiResponse.<PagedResponse<AiConsultationLogResponse>>builder()
                .status(200)
                .message(List.of("Lấy danh sách log thành công"))
                .data(PagedResponse.fromPage(logs))
                .build();
    }

}


