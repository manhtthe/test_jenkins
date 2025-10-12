package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.temp_models.PurchasedServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchasedServicePackageRepository
        extends JpaRepository<PurchasedServicePackage, UUID>,
        JpaSpecificationExecutor<PurchasedServicePackage> {
}

