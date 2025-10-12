package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.booking.models.BookingPackageKol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingPackageKolRepository extends JpaRepository<BookingPackageKol, UUID> {
    List<BookingPackageKol> findByPurchasedPackageId(UUID purchasedPackageId);
}
