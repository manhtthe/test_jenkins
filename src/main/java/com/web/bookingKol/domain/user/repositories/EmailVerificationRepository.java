package com.web.bookingKol.domain.user.repositories;


import com.web.bookingKol.domain.user.models.EmailVerification;
import com.web.bookingKol.domain.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findByUserAndCodeAndUsedFalse(User user, String code);

}
