package com.web.bookingKol.domain.user.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserStatusRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private String status;
}

