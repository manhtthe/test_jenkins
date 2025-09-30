package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.temp_models.PurchasedCoursePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PurchasedCoursePackageRepository extends JpaRepository<PurchasedCoursePackage, UUID>,
        JpaSpecificationExecutor<PurchasedCoursePackage> {
}


