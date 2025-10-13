package com.web.bookingKol.domain.payment.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SePayWebhookRequest {
    private Long id;
    private String gateway;
    private String transactionDate;
    private String accountNumber;
    private String code;
    private String content;
    private String transferType;
    private BigDecimal transferAmount;
    private BigDecimal accumulated;
    private String subAccount;
    private String referenceCode;
    private String description;
}
