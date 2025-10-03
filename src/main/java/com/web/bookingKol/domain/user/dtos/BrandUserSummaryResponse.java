package com.web.bookingKol.domain.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class BrandUserSummaryResponse {
    private UUID userId;
    private String email;
    private String fullName;
    private String status;
}


