package com.web.bookingKol.domain.kol.services;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.dtos.KolAvailabilityDTO;
import com.web.bookingKol.domain.kol.dtos.TimeSlotDTO;
import com.web.bookingKol.domain.kol.models.KolAvailability;
import com.web.bookingKol.domain.kol.models.KolWorkTimeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface KolAvailabilityService {
    ApiResponse<List<KolAvailabilityDTO>> getKolSchedule(UUID userId, OffsetDateTime start, OffsetDateTime end);

    ApiResponse<KolAvailabilityDTO> createKolSchedule(UUID kolId, KolAvailabilityDTO availabilityDTO);

    ApiResponse<KolAvailabilityDTO> getKolAvailabilityById(UUID availabilityId);

    ApiResponse<List<KolAvailabilityDTO>> getKolAvailabilitiesByKol(
            UUID kolId,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            int page,
            int size
    );

    ApiResponse<Page<TimeSlotDTO>> getKolFreeTimes(
            UUID kolId,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            Pageable pageable
    );

    ApiResponse<KolWorkTimeDTO> updateKolWorkTimeByAdmin(UUID workTimeId, KolWorkTimeDTO dto);

}