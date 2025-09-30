package com.web.bookingKol.domain.user.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateProfileRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 255, message = "Họ và tên tối đa 255 ký tự")
    private String fullName;

    @Pattern(regexp = "^(Male|Female|Other)?$", message = "Giới tính chỉ được Male, Female hoặc Other")
    private String gender;

    @Pattern(regexp = "^[0-9]{9,11}$", message = "Số điện thoại phải từ 9–11 chữ số")
    private String phone;

    @Size(max = 500, message = "Địa chỉ tối đa 500 ký tự")
    private String address;

    @Size(max = 1000, message = "Giới thiệu tối đa 1000 ký tự")
    private String introduction;

    @Size(max = 255, message = "Tên thương hiệu tối đa 255 ký tự")
    private String brandName;

    private LocalDate dateOfBirth;

    @Size(max = 255, message = "Tên quốc gia tối đa 255 ký tự")
    private String country;
}


