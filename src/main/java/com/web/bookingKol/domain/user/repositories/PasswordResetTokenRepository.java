package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.user.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByEmailAndToken(String email, String token);
}

