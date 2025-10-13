package com.web.bookingKol.domain.payment.services;

import com.web.bookingKol.domain.payment.dtos.MerchantDTO;
import com.web.bookingKol.domain.payment.dtos.MerchantRequest;
import com.web.bookingKol.domain.payment.models.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface MerchantService {
    Page<MerchantDTO> getAllMerchant(Pageable pageable);

    MerchantDTO getDetailMerchant(UUID merchantId);

    MerchantDTO createMerchant(MerchantRequest merchantRequest);

    MerchantDTO activateMerchant(UUID merchantId);

    Merchant getMerchantIsActive();
}
