package com.web.bookingKol.domain.kol.rest;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.dtos.KolAvailabilityDTO;
import com.web.bookingKol.domain.kol.dtos.TimeSlotDTO;
import com.web.bookingKol.domain.kol.models.KolAvailability;
import com.web.bookingKol.domain.kol.models.KolWorkTimeDTO;
import com.web.bookingKol.domain.kol.services.KolAvailabilityService;
import com.web.bookingKol.domain.kol.services.impl.KolAvailabilityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/kol/availabilities")
@RequiredArgsConstructor
public class KolAvailabilityController {

    private final KolAvailabilityService availabilityService;
    private final KolAvailabilityServiceImpl kolAvailabilityServiceImpl;
    private final KolAvailabilityService kolAvailabilityService;

//    @PreAuthorize("hasAuthority('KOL')")
//    @GetMapping("/{kolId}/schedule")
//    public ResponseEntity<ApiResponse<List<KolAvailabilityDTO>>> getKolSchedule(
//            @PathVariable UUID kolId,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end
//    ) {
//        return ResponseEntity.ok(availabilityService.getKolSchedule(kolId, start, end));
//    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','KOL','LIVE')")
    @PostMapping("/schedule/{kolId}")
    public ResponseEntity<ApiResponse<KolAvailabilityDTO>> createKolAvailability(
            @PathVariable UUID kolId,
            @RequestBody KolAvailabilityDTO availabilityDTO
    ) {

        return ResponseEntity.ok(availabilityService.createKolSchedule(kolId,availabilityDTO));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','KOL','LIVE')")
    @GetMapping("/time-line/{availabilityId}")
    public ResponseEntity<ApiResponse<KolAvailabilityDTO>> getKolAvailabilityById(
            @PathVariable UUID availabilityId
    ) {
        return ResponseEntity.ok(availabilityService.getKolAvailabilityById(availabilityId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','KOL','LIVE')")
    @GetMapping("/time-line/kol/{kolId}")
    public ResponseEntity<ApiResponse<List<KolAvailabilityDTO>>> getKolAvailabilitiesByKol(
            @PathVariable UUID kolId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(availabilityService.getKolAvailabilitiesByKol(kolId, startDate, endDate, page, size));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','USER','LIVE','KOL')")
    @GetMapping("/free-time/{kolId}")
    public ResponseEntity<ApiResponse<Page<TimeSlotDTO>>> getKolFreeTimes(
            @PathVariable UUID kolId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startAt").ascending());
        return ResponseEntity.ok(kolAvailabilityServiceImpl.getKolFreeTimes(kolId, startDate, endDate, pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/schedule/{workTimeId}")
    public ResponseEntity<ApiResponse<KolWorkTimeDTO>> updateKolWorkTime(
            @PathVariable UUID workTimeId,
            @RequestBody KolWorkTimeDTO dto
    ) {
        return ResponseEntity.ok(kolAvailabilityService.updateKolWorkTimeByAdmin(workTimeId, dto));
    }

}