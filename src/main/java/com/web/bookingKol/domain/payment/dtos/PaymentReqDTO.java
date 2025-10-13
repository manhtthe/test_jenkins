package com.web.bookingKol.domain.payment.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentReqDTO {
    private UUID bookingRequestId;
    private UUID contractId;
    private BigDecimal amount;
    private String qrUrl;
    private UUID userId;

    private String ownerName;
    private String bank;
    private String accountNumber;
}
