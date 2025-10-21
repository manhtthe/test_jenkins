package com.web.bookingKol.domain.kol.repositories;

import com.web.bookingKol.domain.kol.models.KolAvailability;
import com.web.bookingKol.domain.kol.models.KolWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface KolWorkTimeRepository extends JpaRepository<KolWorkTime, UUID> {
    @Query("""
        SELECT wt FROM KolWorkTime wt
        WHERE wt.availability.kol.id = :kolId
          AND wt.status = 'BOOKED'
          AND (CAST(:startDate AS timestamp) IS NULL OR wt.endAt >= :startDate)
          AND (CAST(:endDate AS timestamp) IS NULL OR wt.startAt <= :endDate)
        ORDER BY wt.startAt ASC
    """)
    List<KolWorkTime> findBookedTimes(
            @Param("kolId") UUID kolId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );




    List<KolWorkTime> findByAvailability_Id(UUID availabilityId);


}
