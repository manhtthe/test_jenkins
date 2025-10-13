package com.web.bookingKol.domain.user.repositories;

import com.web.bookingKol.domain.user.dtos.BrandUserSummaryResponse;
import com.web.bookingKol.domain.user.models.Brand;
import com.web.bookingKol.domain.user.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
    Optional<Brand> findByUser(User user);

    @Query("""
SELECT new com.web.bookingKol.domain.user.dtos.BrandUserSummaryResponse(
    u.id, u.email, u.fullName, u.status
)
FROM User u
JOIN u.roles r
JOIN Brand b ON b.user = u
WHERE r.key = 'USER'
  AND (:search = '' OR :search IS NULL
       OR LOWER(u.email) LIKE LOWER(CONCAT('%', CAST(:search as string), '%'))
       OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', CAST(:search as string), '%')))
""")

    Page<BrandUserSummaryResponse> findBrandUsers(@Param("search") String search, Pageable pageable);

}

