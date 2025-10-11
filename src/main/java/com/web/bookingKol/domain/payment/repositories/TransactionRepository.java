package com.web.bookingKol.domain.payment.repositories;

import com.web.bookingKol.domain.payment.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
