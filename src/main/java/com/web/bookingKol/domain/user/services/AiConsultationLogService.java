package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.domain.user.dtos.AiConsultationLogResponse;

import java.util.List;

public interface AiConsultationLogService {
    List<AiConsultationLogResponse> getAllLogs();
}

