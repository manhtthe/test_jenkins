package com.web.bookingKol.auth.dtos;

import lombok.Data;

@Data
public class BrandRegisterRequestDTO {
    private String email;
    private String password;
    private String brandName;
    private String contactPerson;
    private String contactPhone;
}


