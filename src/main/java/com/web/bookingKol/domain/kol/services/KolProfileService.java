package com.web.bookingKol.domain.kol.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import com.web.bookingKol.domain.kol.dtos.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    ApiResponse<KolCreatedDTO> createNewKolAccount(UUID AdminId, NewKolDTO newKolDTO, MultipartFile fileAvatar);

    ApiResponse<KolDetailDTO> updateKolProfile(UUID AdminId, UUID kolId, UpdateKolDTO updateKolDTO);

    ApiResponse<List<FileUsageDTO>> uploadKolMedias(UUID uploaderId, UUID kolId, List<MultipartFile> files);

    ApiResponse<List<FileUsageDTO>> getAllKolMediaFiles(UUID kolId);

    ApiResponse<?> activateOrDeactivateKolMediaFile(UUID kolId, List<UUID> fileUsageIds, boolean isActive);

    ApiResponse<FileUsageDTO> setAvatarWithExistedImage(UUID kolId, UUID fileId);

    ApiResponse<FileUsageDTO> setAvatarWithUploadNewImage(UUID adminId, UUID kolId, MultipartFile fileAvatar);
}
