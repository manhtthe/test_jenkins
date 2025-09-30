package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.user.models.Brand;
import com.web.bookingKol.domain.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
    Optional<Brand> findByUser(User user);
}

