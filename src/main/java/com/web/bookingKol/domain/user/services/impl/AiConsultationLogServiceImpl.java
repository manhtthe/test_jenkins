package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.domain.user.dtos.AiConsultationLogResponse;
import com.web.bookingKol.domain.user.repositories.AiConsultationLogRepository;
import com.web.bookingKol.domain.user.services.AiConsultationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiConsultationLogServiceImpl implements AiConsultationLogService {

    private final AiConsultationLogRepository logRepository;

    @Override
    public List<AiConsultationLogResponse> getAllLogs() {
        return logRepository.findAll()
                .stream()
                .map(log -> new AiConsultationLogResponse(
                        log.getId(),
                        log.getQuestion(),
                        log.getAnswer(),
                        log.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}

