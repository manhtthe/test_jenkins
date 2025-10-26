package com.web.bookingKol.domain.payment.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantRequest {
    private String name;
    @NotNull
    private String apiKey;
    @NotNull
    private String bank;
    @NotNull
    private String accountNumber;
    private String vaNumber;
}
