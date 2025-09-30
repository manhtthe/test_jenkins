package com.web.bookingKol.domain.kol.dtos;

import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class NewKolDTO {
    private String fullName;
    private String displayName;
    private Date dob;
    private String email;
    private String phone;
    private String password;
    private String gender;
    private String address;
    private String avatarUrl;
    private String status;
    private String bio;
    private String experience;
    private String country;
    private String city;
    private String languages;
    private String rateCardNote;
    private BigDecimal minBookingPrice;

    private Set<UUID> categoryIds;
    private Set<FileUsageDTO> fileUsageDtos;
}
