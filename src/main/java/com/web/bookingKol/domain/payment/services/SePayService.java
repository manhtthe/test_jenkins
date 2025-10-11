package com.web.bookingKol.domain.payment.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.bookingKol.common.Enums;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.config.TimezoneConfig;
import com.web.bookingKol.domain.booking.repositories.ContractRepository;
import com.web.bookingKol.domain.payment.dtos.SePayWebhookRequest;
import com.web.bookingKol.domain.payment.dtos.TransactionResult;
import com.web.bookingKol.domain.payment.mappers.TransactionMapper;
import com.web.bookingKol.domain.payment.models.Transaction;
import com.web.bookingKol.domain.payment.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class SePayService {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private ContractRepository contractRepository;

    private final TransactionRepository transactionRepository;
    private TimezoneConfig timezoneConfig;
    private final ZoneId ZONE;

    public SePayService(TransactionRepository transactionRepository,
                        TimezoneConfig timezoneConfig) {
        this.transactionRepository = transactionRepository;
        this.ZONE = timezoneConfig.getZoneId();
    }

    private final String SEPAY_API_URL = "https://qr.sepay.vn/img?";
    private final String ACCOUNT = "96247LQACX";
    private final String BANK = "BIDV";
    private final String DESCRIPTION = "chuyen tien";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createQRCode(UUID contractId, BigDecimal amount) {
        return UriComponentsBuilder.fromUriString(SEPAY_API_URL)
                .queryParam("acc", ACCOUNT)
                .queryParam("bank", BANK)
                .queryParam("amount", amount != null ? amount : "")
                .queryParam("des", DESCRIPTION + ":" + contractId)
                .toUriString();
    }

    @Transactional
    public ApiResponse<TransactionResult> handleWebhook(SePayWebhookRequest request) {
        try {
            LocalDateTime transactionDate = LocalDateTime.parse(
                    request.getTransactionDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            String content = request.getContent();

            Transaction tx = Transaction.builder()
                    .gateway(request.getGateway())
                    .transactionDate(transactionDate.atZone(ZONE).toInstant())
                    .accountNumber(request.getAccountNumber())
                    .subAccount(request.getSubAccount())
                    .amountIn(request.getTransferType().equalsIgnoreCase("in") ? request.getTransferAmount() : BigDecimal.ZERO)
                    .amountOut(request.getTransferType().equalsIgnoreCase("out") ? request.getTransferAmount() : BigDecimal.ZERO)
                    .accumulated(request.getAccumulated())
                    .code(request.getCode())
                    .transactionContent(request.getContent())
                    .referenceNumber(request.getReferenceCode())
                    .createdAt(Instant.now())
                    .body(objectMapper.writeValueAsString(request))
                    .build();
            UUID contractId = null;

            if (content != null) {
                contractId = UUID.fromString(content.trim().substring(content.indexOf(":") + 1));
                contractRepository.findById(contractId).ifPresent(contract -> tx.setPayment(contract.getPayment()));
            }
            transactionRepository.save(tx);
            paymentService.updatePayment(transactionMapper.toDto(tx));
            TransactionResult tr = TransactionResult.builder()
                    .contractId(contractId)
                    .status(Enums.TransactionStatus.COMPLETED.name())
                    .transactionId(tx.getId())
                    .build();
            return ApiResponse.<TransactionResult>builder()
                    .status(HttpStatus.OK.value())
                    .message(List.of("Transaction successfully!"))
                    .data(tr)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error saving webhook transaction: " + e.getMessage(), e);
        }
    }

}


