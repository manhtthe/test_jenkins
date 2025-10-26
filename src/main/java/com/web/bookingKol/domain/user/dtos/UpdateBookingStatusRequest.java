package com.web.bookingKol.domain.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookingStatusRequest {

    @NotBlank(message = "Trạng thái không được để trống")
    private String status;
}

