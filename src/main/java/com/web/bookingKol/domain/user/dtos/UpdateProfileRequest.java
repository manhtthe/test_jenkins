package com.web.bookingKol.domain.user.dtos;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateProfileRequest {

    @Size(max = 255, message = "Họ và tên tối đa 255 ký tự")
    private String fullName;

    @Size(max = 50, message = "Giới tính tối đa 50 ký tự")
    private String gender;

    @Size(max = 11, message = "Số điện thoại tối đa 11 ký tự")
    private String phone;

    @Size(max = 255, message = "Quốc gia tối đa 255 ký tự")
    private String country;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate dateOfBirth;

    @Size(max = 1000, message = "Giới thiệu tối đa 1000 ký tự")
    private String introduction;

    @Size(max = 255, message = "Tên thương hiệu tối đa 255 ký tự")
    private String brandName;
}

