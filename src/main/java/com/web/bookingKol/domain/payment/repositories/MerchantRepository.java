package com.web.bookingKol.domain.payment.repositories;

import com.web.bookingKol.domain.payment.models.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    @Modifying
    @Query("UPDATE Merchant m SET m.isActive = false WHERE m.id <> :activeMerchantId")
    void deactivateAllOthers(@Param("activeMerchantId") UUID activeMerchantId);

    @Modifying
    @Query("UPDATE Merchant m SET m.isActive = false")
    void deactivateAll();

    @Query("SELECT m FROM Merchant m WHERE m.isActive = true")
    Merchant findMerchantIsActive();
}
