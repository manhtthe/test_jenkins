package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.temp_models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
}

