package com.web.bookingKol.auth;

import com.web.bookingKol.auth.dtos.LoginRequestDTO;
import com.web.bookingKol.common.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.web.bookingKol.auth.dtos.BrandRegisterRequestDTO;
import com.web.bookingKol.common.payload.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth")
public class AuthRestController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO, HttpServletRequest request, HttpServletResponse response) {
        return authService.login(loginRequestDTO, request, response);
    }

    @PostMapping("/register/brand")
    public ResponseEntity<ApiResponse<?>> registerBrand(@RequestBody BrandRegisterRequestDTO request)
            throws UserAlreadyExistsException {
        return authService.registerBrand(request);
    }


    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyEmail(
            @RequestParam String email,
            @RequestParam String code) {
        return authService.verifyEmaildk(email, code);
    }
}
