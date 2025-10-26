package com.web.bookingKol.domain.kol.repositories;

import com.web.bookingKol.domain.kol.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c WHERE c.id = :categoryId")
    Optional<Category> findByCategoryId(@Param("categoryId") UUID categoryId);

}
