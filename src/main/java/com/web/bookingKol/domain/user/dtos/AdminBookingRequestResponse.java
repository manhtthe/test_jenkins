package com.web.bookingKol.domain.user.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AdminBookingRequestResponse {

    private UUID bookingRequestId;
    private String description;
    private String status;
    private String repeatType;
    private String dayOfWeek;
    private LocalDate repeatUntil;
    private BigDecimal contractAmount;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID campaignId;
    private String campaignName;
    private String campaignObjective;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private LocalDate startDate;
    private LocalDate endDate;
    private String createdByEmail;

    private List<KolInfo> kols;
    private List<KolInfo> lives;

    private UUID contractId;
    private String contractNumber;
    private String contractStatus;
    private String contractTerms;
    private String contractFileUrl;
}


