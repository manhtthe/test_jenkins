package com.web.bookingKol.domain.user.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BookKolRequest {
    private UUID packageId;
    private List<UUID> kolIds;

    private String campaignName;
    private String objective;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private LocalDate startDate;
    private LocalDate endDate;
    private String recurrencePattern;
    private List<UUID> liveIds;
}
