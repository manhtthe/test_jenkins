package com.web.bookingKol.domain.booking.mappers;

import com.web.bookingKol.domain.booking.dtos.ContractDTO;
import com.web.bookingKol.domain.booking.models.Contract;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    ContractDTO toDto(Contract contract);

    Set<ContractDTO> toDtoSet(Set<Contract> contracts);
}
