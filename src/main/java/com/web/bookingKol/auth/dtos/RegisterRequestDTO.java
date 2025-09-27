package com.web.bookingKol.auth.dtos;

import com.web.bookingKol.common.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO implements Serializable {


    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    @StrongPassword
    private String password;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Phone is required!")
    @Pattern(regexp = "^[0-9]{9,11}$", message = "Số điện thoại phải từ 9–11 chữ số")
    private String phone;

}
