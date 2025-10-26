package com.web.bookingKol.domain.payment.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class PaymentReqDTO {
    private UUID contractId;
    private BigDecimal amount;
    private String qrUrl;
    private UUID userId;
    private String transferContent;
    private Instant expiresAt;

    private String name;
    private String bank;
    private String accountNumber;
}
