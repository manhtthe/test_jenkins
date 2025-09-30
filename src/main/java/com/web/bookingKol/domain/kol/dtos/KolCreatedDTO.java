package com.web.bookingKol.domain.kol.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class KolCreatedDTO {
    private UUID id;
    private UUID userId;
    private String fullName;
    private String displayName;
    private String email;
    private String phone;
    private String avatarUrl;
    private String gender;
    private String status;
    private Date dob;
    private String bio;
    private String experience;
    private String country;
    private String city;
    private String languages;
    private String rateCardNote;
    private BigDecimal minBookingPrice;
    private Boolean isAvailable;
    private Instant createdAt;

    private Set<CategoryDTO> categories;
}
