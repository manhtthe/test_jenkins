package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.domain.user.dtos.AiConsultationLogResponse;
import com.web.bookingKol.domain.user.models.AiConsultationLog;
import com.web.bookingKol.domain.user.repositories.AiConsultationLogRepository;
import com.web.bookingKol.domain.user.services.AiConsultationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiConsultationLogServiceImpl implements AiConsultationLogService {

    private final AiConsultationLogRepository logRepository;

    @Override
    public Page<AiConsultationLogResponse> getAllLogs(String search, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<AiConsultationLog> spec = (root, query, cb) -> cb.conjunction();

        // Search
        if (search != null && !search.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("question")), "%" + search.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("answer")), "%" + search.toLowerCase() + "%")
                    ));
        }

        if (startDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), start));
        }

        if (endDate != null) {
            LocalDateTime end = endDate.plusDays(1).atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.lessThan(root.get("createdAt"), end));
        }

        return logRepository.findAll(spec, pageable)
                .map(log -> new AiConsultationLogResponse(
                        log.getId(),
                        log.getQuestion(),
                        log.getAnswer(),
                        log.getCreatedAt()
                ));
    }
}

