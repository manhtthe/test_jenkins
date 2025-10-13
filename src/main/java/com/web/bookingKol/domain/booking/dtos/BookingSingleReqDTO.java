package com.web.bookingKol.domain.booking.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class BookingSingleReqDTO {
    @NotNull
    private UUID kolId;
    @NotNull
    private Instant startAt;
    @NotNull
    private Instant endAt;
    private UUID campaignId;
    private UUID kolPromoId;
    private String description;
    private String location;

    @AssertTrue(message = "You must agree to the terms before continuing.")
    private Boolean isConfirmWithTerms;
}
