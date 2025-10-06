package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.user.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
}

