package com.web.bookingKol.auth;

import com.web.bookingKol.auth.dtos.BrandRegisterRequestDTO;
import com.web.bookingKol.common.exception.UserAlreadyExistsException;
import com.web.bookingKol.common.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class AuthRegisController {
    @Autowired
    private AuthService authService;

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyEmail(
            @RequestParam String email,
            @RequestParam String code) {
        return authService.verifyEmaildk(email, code);
    }

    @PostMapping("/brand")
    public ResponseEntity<ApiResponse<?>> registerBrand(@RequestBody BrandRegisterRequestDTO request)
            throws UserAlreadyExistsException {
        return authService.registerBrand(request);
    }
}
