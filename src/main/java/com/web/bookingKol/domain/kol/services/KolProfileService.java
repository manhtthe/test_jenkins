package com.web.bookingKol.domain.kol.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.dtos.FilterKolDTO;
import com.web.bookingKol.domain.kol.dtos.KolDetailDTO;
import com.web.bookingKol.domain.kol.dtos.KolProfileDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface KolProfileService {
    ApiResponse<KolDetailDTO> getKolProfileByUserId(UUID userId);

    ApiResponse<KolDetailDTO> getKolProfileByKolId(UUID kolId);

    ApiResponse<List<KolProfileDTO>> getAllKol();

    ApiResponse<List<KolProfileDTO>> getAllKolAvailable();

    ApiResponse<List<KolProfileDTO>> getAllKolProfilesByCategoryId(UUID categoryId);

    ApiResponse<List<KolProfileDTO>> getAllKolWithFilter(FilterKolDTO filterKolDTO);
}
