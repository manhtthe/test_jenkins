package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.domain.user.dtos.AiConsultationLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AiConsultationLogService {
    Page<AiConsultationLogResponse> getAllLogs(String search, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
