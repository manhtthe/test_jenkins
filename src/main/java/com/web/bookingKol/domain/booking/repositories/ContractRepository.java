package com.web.bookingKol.domain.booking.repositories;

import com.web.bookingKol.domain.booking.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
}
