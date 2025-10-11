package com.web.bookingKol.domain.booking.dtos;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ContractDTO {
    private UUID id;
    private UUID bookingRequestId;
    private String status;
    private String terms;
    private Instant signedAtBrand;
    private Instant signedAtKol;
    private Instant createdAt;
    private Instant updatedAt;
}
