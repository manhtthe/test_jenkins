package com.web.bookingKol.domain.payment.services.impl;

import com.web.bookingKol.common.Enums;
import com.web.bookingKol.domain.booking.models.BookingRequest;
import com.web.bookingKol.domain.booking.models.Contract;
import com.web.bookingKol.domain.booking.services.BookingRequestService;
import com.web.bookingKol.domain.payment.dtos.PaymentReqDTO;
import com.web.bookingKol.domain.payment.dtos.TransactionDTO;
import com.web.bookingKol.domain.payment.models.Payment;
import com.web.bookingKol.domain.payment.repositories.PaymentRepository;
import com.web.bookingKol.domain.payment.services.PaymentService;
import com.web.bookingKol.domain.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BookingRequestService bookingRequestService;

    private final String VND_CURRENCY = "VND";

    @Override
    public PaymentReqDTO initiatePayment(BookingRequest bookingRequest, Contract contract, String qrUrl, User user, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setContract(contract);
        payment.setUser(user);
        payment.setTotalAmount(amount);
        payment.setCurrency(VND_CURRENCY);
        payment.setStatus(Enums.PaymentStatus.PENDING.name());
        payment.setPaidAmount(null);
        payment.setFailureReason(null);
        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(null);
        paymentRepository.save(payment);
        return PaymentReqDTO.builder()
                .bookingRequestId(bookingRequest.getId())
                .contractId(contract.getId())
                .qrUrl(qrUrl)
                .userId(user.getId())
                .amount(contract.getAmount())
                .build();
    }

    @Transactional
    @Override
    public void updatePayment(TransactionDTO transactionDTO) {
        Payment payment = paymentRepository.findById(transactionDTO.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        BigDecimal currentPaidAmount = payment.getPaidAmount() == null ? BigDecimal.ZERO : payment.getPaidAmount();
        BigDecimal newPaidAmount = currentPaidAmount.add(transactionDTO.getAmountIn());
        payment.setPaidAmount(newPaidAmount);
        payment.setUpdatedAt(Instant.now());
        BigDecimal totalAmount = payment.getTotalAmount();
        int compare = newPaidAmount.compareTo(totalAmount);
        payment.setUpdatedAt(Instant.now());
        String status = switch (compare) {
            case -1 -> Enums.PaymentStatus.UNDERPAID.name();
            case 0 -> Enums.PaymentStatus.PAID.name();
            default -> Enums.PaymentStatus.OVERPAID.name();
        };
        if (status.equals(Enums.PaymentStatus.PAID.name()) || status.equals(Enums.PaymentStatus.OVERPAID.name())) {
            bookingRequestService.acceptBookingRequest(payment.getContract().getBookingRequest());
        }
        payment.setStatus(status);
        paymentRepository.save(payment);
    }
}
