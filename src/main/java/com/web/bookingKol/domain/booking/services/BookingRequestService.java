package com.web.bookingKol.domain.booking.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.booking.dtos.BookingSingleReqDTO;
import com.web.bookingKol.domain.booking.dtos.BookingSingleResDTO;
import com.web.bookingKol.domain.payment.dtos.PaymentReqDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public interface BookingRequestService {
    ApiResponse<PaymentReqDTO> createBookingSingleReq(UUID userId, BookingSingleReqDTO bookingRequestDTO, List<MultipartFile> attachedFiles);

    ApiResponse<List<BookingSingleResDTO>> getAllRequestAdmin(UUID kolId,
                                                              String status,
                                                              LocalDate startAt,
                                                              LocalDate endAt,
                                                              LocalDate createdAtFrom,
                                                              LocalDate createdAtTo,
                                                              int page,
                                                              int size);

    ApiResponse<BookingSingleResDTO> getDetailBooking(UUID bookingRequestId);
}
