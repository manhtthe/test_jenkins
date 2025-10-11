package com.web.bookingKol.domain.payment.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class PaymentDTO {
    private UUID id;
    private UUID contractId;
    private UUID userId;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String currency;
    private String status;
    private String failureReason;
    private Instant createdAt;
    private Instant updatedAt;

    private Set<UUID> refundIds;
    private Set<UUID> transactionIds;
}
