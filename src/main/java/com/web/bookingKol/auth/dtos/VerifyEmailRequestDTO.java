package com.web.bookingKol.auth.dtos;

import lombok.Data;

@Data
public class VerifyEmailRequestDTO {
    private String email;
    private String code;
}

