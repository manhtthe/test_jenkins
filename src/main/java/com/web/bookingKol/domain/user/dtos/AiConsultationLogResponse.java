package com.web.bookingKol.domain.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AiConsultationLogResponse {
    private UUID id;
    private String question;
    private String answer;
    private Instant createdAt;
}

