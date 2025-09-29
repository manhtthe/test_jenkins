package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.temp_models.PurchasedCoursePackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PurchasedCoursePackageRepository extends JpaRepository<PurchasedCoursePackage, UUID> {
    List<PurchasedCoursePackage> findByUser(User user);
}

