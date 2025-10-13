package com.web.bookingKol.domain.booking.dtos;

import java.time.Instant;

public record SoftHoldDetails(
        String userId,
        Instant startAt,
        Instant endAt
) {
}
