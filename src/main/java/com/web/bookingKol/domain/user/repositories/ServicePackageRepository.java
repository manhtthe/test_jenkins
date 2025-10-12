package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.temp_models.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, UUID> {
    List<ServicePackage> findByPackageTypeIgnoreCase(String packageType);
}

