package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.booking.models.Contract;
import com.web.bookingKol.domain.booking.models.Review;
import com.web.bookingKol.domain.kol.models.KolProfile;
import com.web.bookingKol.domain.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, java.util.UUID> {
    boolean existsByContractAndReviewerAndKol(Contract contract, User reviewer, KolProfile kol);
}

