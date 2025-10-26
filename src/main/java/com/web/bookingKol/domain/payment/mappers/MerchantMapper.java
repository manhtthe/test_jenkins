package com.web.bookingKol.domain.payment.mappers;

import com.web.bookingKol.domain.payment.dtos.MerchantDTO;
import com.web.bookingKol.domain.payment.models.Merchant;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MerchantMapper {
    MerchantDTO toDto(Merchant merchant);

    Merchant toEntity(MerchantDTO merchantDTO);

    List<MerchantDTO> toDtoList(List<Merchant> merchants);
}
