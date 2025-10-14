package com.web.bookingKol.domain.kol.services;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.dtos.KolAvailabilityDTO;
import com.web.bookingKol.domain.kol.models.KolAvailability;

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

}