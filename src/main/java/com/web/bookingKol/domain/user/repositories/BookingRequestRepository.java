package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.booking.models.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest, UUID> {
}

