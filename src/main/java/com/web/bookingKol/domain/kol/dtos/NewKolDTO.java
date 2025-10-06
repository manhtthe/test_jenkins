package com.web.bookingKol.domain.kol.dtos;

import com.web.bookingKol.common.StrongPassword;
import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class NewKolDTO implements Serializable {
    private String fullName;
    private String displayName;
    private Date dob;
    @NotNull(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    private String phone;
    @NotNull(message = "Mật khẩu không được để trống")
    @StrongPassword
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
