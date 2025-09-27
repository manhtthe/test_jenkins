package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.user.models.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BlacklistedTokenRepository extends JpaRepository<com.web.bookingKol.domain.user.models.BlacklistedToken, UUID> {
    Optional<BlacklistedToken> findByToken(String token);
    boolean existsByToken(String token);
}

