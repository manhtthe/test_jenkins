package com.web.bookingKol.domain.kol.dtos;

import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class KolProfileDTO {
    private UUID id;
    private UUID userId;
    private String displayName;
    private String country;
    private String experience;
    private String languages;
    private String rateCardNote;
    private BigDecimal minBookingPrice;
    private Boolean isAvailable;
    private Double overallRating;
    private Integer feedbackCount;

    private Set<CategoryDTO> categories;
    private Set<FileUsageDTO> fileUsageDtos;
}
