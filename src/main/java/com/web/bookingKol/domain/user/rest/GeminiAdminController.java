package com.web.bookingKol.domain.user.rest;

import com.web.bookingKol.domain.user.dtos.AiConsultationLogResponse;
import com.web.bookingKol.domain.user.services.AiConsultationLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "GEMINI ADMIN")
@RequestMapping("/adminconsultation")
@RequiredArgsConstructor
public class GeminiAdminController {

    private final AiConsultationLogService logService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("/logs")
    public List<AiConsultationLogResponse> getLogs() {
        return logService.getAllLogs();
    }
}

