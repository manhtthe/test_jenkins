package com.web.bookingKol.domain.kol.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import com.web.bookingKol.domain.kol.dtos.*;
import com.web.bookingKol.domain.kol.models.KolProfile;
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

    //KOL personal information management
    ApiResponse<KolCreatedDTO> createNewKolAccount(UUID AdminId, NewKolDTO newKolDTO, MultipartFile fileAvatar);

    ApiResponse<KolDetailDTO> updateKolProfile(UUID changerId, UUID kolId, UpdateKolDTO updateKolDTO);

    //KOL media management
    ApiResponse<List<FileUsageDTO>> uploadKolMedias(UUID uploaderId, UUID kolId, List<MultipartFile> files);

    ApiResponse<List<FileUsageDTO>> getAllKolMediaFiles(UUID kolId);

    ApiResponse<?> activateOrDeactivateKolMediaFile(UUID kolId, List<UUID> fileUsageIds, boolean isActive);

    ApiResponse<FileUsageDTO> setCoverImage(UUID kolId, UUID fileId);

    ApiResponse<FileUsageDTO> setAvatarWithExistedImage(UUID kolId, UUID fileId);

    ApiResponse<FileUsageDTO> setAvatarWithUploadNewImage(UUID changerId, UUID kolId, MultipartFile fileAvatar);

    //KOL category management
    ApiResponse<KolDetailDTO> addCategoryForKol(UUID changerId, UUID kolId, UUID categoryId);

    ApiResponse<KolDetailDTO> removeCategoryForKol(UUID changerId, UUID kolId, UUID categoryId);

    //Helper method
    KolProfile getKolProfileEntityByUserId(UUID userId);
}
