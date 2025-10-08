package com.web.bookingKol.domain.kol.repositories;

import com.web.bookingKol.domain.kol.models.KolAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface KolAvailabilityRepository extends JpaRepository<KolAvailability, UUID> {
//    List<KolAvailability> findByUser(User user);
//
//    List<KolAvailability> findByUserAndStartAtBetween(User user, OffsetDateTime start, OffsetDateTime end);

    @Query(value = """
                SELECT * FROM kol_availabilities WHERE kol_id = :kolId
                  AND (COALESCE(:start, start_at) <= start_at)
                  AND (COALESCE(:end, end_at) >= end_at)
                ORDER BY start_at
            """, nativeQuery = true)
    List<KolAvailability> findByKolIdAndDateRange(
            @Param("kolId") UUID kolId,
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end
    );

    @Query("""
                SELECT CASE WHEN COUNT(ka) > 0 THEN TRUE ELSE FALSE END
                FROM KolAvailability ka
                WHERE ka.kol.id = :kolId
                  AND ka.status = 'AVAILABLE'
                  AND :start >= ka.startAt
                  AND :end <= ka.endAt
            """)
    boolean isKolAvailabilityInRange(
            @Param("kolId") UUID kolId,
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end
    );


}

