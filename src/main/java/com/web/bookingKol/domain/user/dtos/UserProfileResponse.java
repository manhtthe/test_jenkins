package com.web.bookingKol.domain.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String email;
    private String fullName;
    private String gender;
    private String phone;
    private String address;
    private String introduction;
    private String brandName;
    private LocalDate dateOfBirth;
    private String country;
}

