package com.web.bookingKol.domain.user.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class AdminCreateBookingRequestDTO {
    private UUID campaignId;
    private String description;
    private String status;
    private String repeatType;
    private String dayOfWeek;
    private Instant startAt;
    private LocalDate repeatUntil;
    private BigDecimal contractAmount;

    private MultipartFile contractFile;

    private List<UUID> kolIds;
    private List<UUID> liveIds;
}

