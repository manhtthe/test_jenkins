package com.web.bookingKol.domain.payment.mappers;

import com.web.bookingKol.domain.payment.dtos.TransactionDTO;
import com.web.bookingKol.domain.payment.models.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDTO toDto(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setGateway(transaction.getGateway());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setAccountNumber(transaction.getAccountNumber());
        dto.setSubAccount(transaction.getSubAccount());
        dto.setAmountIn(transaction.getAmountIn());
        dto.setAmountOut(transaction.getAmountOut());
        dto.setAccumulated(transaction.getAccumulated());
        dto.setCode(transaction.getCode());
        dto.setTransactionContent(transaction.getTransactionContent());
        dto.setReferenceNumber(transaction.getReferenceNumber());
        dto.setBody(transaction.getBody());
        dto.setCreatedAt(transaction.getCreatedAt());
        if (transaction.getPayment() != null) {
            dto.setPaymentId(transaction.getPayment().getId());
        }
        return dto;
    }
}
