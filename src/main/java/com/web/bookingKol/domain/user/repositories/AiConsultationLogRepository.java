package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.user.models.AiConsultationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AiConsultationLogRepository
        extends JpaRepository<AiConsultationLog, UUID>, JpaSpecificationExecutor<AiConsultationLog> {
}


