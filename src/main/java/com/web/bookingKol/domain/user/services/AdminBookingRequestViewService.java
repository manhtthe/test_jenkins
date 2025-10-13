package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.PagedResponse;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.AdminBookingRequestResponse;
import org.springframework.data.domain.Pageable;

public interface AdminBookingRequestViewService {
    ApiResponse<PagedResponse<AdminBookingRequestResponse>> getAllBookingRequests(Pageable pageable);
}

