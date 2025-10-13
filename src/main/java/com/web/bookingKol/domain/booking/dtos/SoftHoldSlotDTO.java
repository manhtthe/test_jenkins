package com.web.bookingKol.domain.booking.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class SoftHoldSlotDTO {
    private UUID kolId;
    private Instant startTimeIso;
    private Instant endTimeIso;
}
