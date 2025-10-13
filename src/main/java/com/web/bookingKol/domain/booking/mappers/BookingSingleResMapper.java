package com.web.bookingKol.domain.booking.mappers;

import com.web.bookingKol.domain.booking.dtos.BookingSingleResDTO;
import com.web.bookingKol.domain.booking.models.BookingRequest;
import com.web.bookingKol.domain.file.mappers.FileUsageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingSingleResMapper {
    @Autowired
    private FileUsageMapper fileUsageMapper;
    @Autowired
    private ContractMapper contractMapper;

    public BookingSingleResDTO toDto(BookingRequest bookingRequest) {
        if (bookingRequest == null) {
            return null;
        }
        BookingSingleResDTO dto = new BookingSingleResDTO();
        dto.setId(bookingRequest.getId());
        dto.setKolId(bookingRequest.getKol().getId());
        dto.setUserId(bookingRequest.getUser().getId());
        dto.setBookingType(bookingRequest.getBookingType());
        dto.setStatus(bookingRequest.getStatus());
        dto.setDescription(bookingRequest.getDescription());
        dto.setLocation(bookingRequest.getLocation());
        dto.setStartAt(bookingRequest.getStartAt());
        dto.setEndAt(bookingRequest.getEndAt());
        dto.setCreatedAt(bookingRequest.getCreatedAt());
        dto.setUpdatedAt(bookingRequest.getUpdatedAt());
        if (bookingRequest.getAttachedFiles() != null) {
            dto.setAttachedFiles(fileUsageMapper.toDtoSet(bookingRequest.getAttachedFiles()));
        }
        if (bookingRequest.getContracts() != null) {
            dto.setContracts(contractMapper.toDtoSet(bookingRequest.getContracts()));
        }
        return dto;
    }
}
