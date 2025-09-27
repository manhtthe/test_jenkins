package com.web.bookingKol.auth;

import com.web.bookingKol.auth.dtos.BrandRegisterRequestDTO;
import com.web.bookingKol.auth.dtos.RegisterRequestDTO;
import com.web.bookingKol.common.exception.UserAlreadyExistsException;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.models.BlacklistedToken;
import com.web.bookingKol.domain.user.repositories.BlacklistedTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/register")
public class AuthRegisController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyEmail(
            @RequestParam String email,
            @RequestParam String code) {
        return authService.verifyEmaildk(email, code);
    }


    @PostMapping("/brand")
    public ResponseEntity<ApiResponse<?>> registerBrand(@Valid @RequestBody RegisterRequestDTO request)
            throws UserAlreadyExistsException {
        return authService.registerBrand(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request,
                                                 @AuthenticationPrincipal UserDetails user) {
        String token = getToken(request);
        if (token == null || !jwtUtils.validateAccessToken(token)) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().status(400).message(List.of("Token không hợp lệ")).data(null).build()
            );

        }
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiredAt(Instant.ofEpochMilli(
                jwtUtils.getExpiration(token) * 1000 + System.currentTimeMillis())
        );
        blacklistedTokenRepository.save(blacklistedToken);

        return ResponseEntity.ok(
                ApiResponse.builder().status(200).message(List.of("Đăng xuất thành công")).data(null).build()
        );

    }


    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
