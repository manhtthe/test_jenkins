package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.BookKolRequest;

import java.util.UUID;

public interface BookingService {
    ApiResponse<?> bookKol(BookKolRequest request, String email);
}


