package com.web.bookingKol.domain.user.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "token_blacklist")
public class BlacklistedToken {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 2000)
    private String token;

    @Column(name = "expired_at", nullable = false)
    private Instant expiredAt;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}

