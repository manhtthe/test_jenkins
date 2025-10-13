package com.web.bookingKol.domain.payment.dtos;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class MerchantDTO {
    private UUID id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String apiKey;
    private String bank;
    private String accountNumber;
    private String vaNumber;
    private Boolean isActive;
}
