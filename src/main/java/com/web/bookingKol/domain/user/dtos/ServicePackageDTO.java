package com.web.bookingKol.domain.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicePackageDTO {
    private UUID id;
    private String name;
    private String description;
    private String packageType;
    private Boolean allowKolSelection;
}

