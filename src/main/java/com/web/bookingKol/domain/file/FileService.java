package com.web.bookingKol.domain.file;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.file.dtos.FileDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface FileService {
    FileDTO uploadFilePoint(UUID uploaderId, MultipartFile file);

    ApiResponse<FileDTO> uploadOneFile(UUID uploaderId, MultipartFile file);

    ApiResponse<List<FileDTO>> uploadMultipleFiles(UUID uploaderId, List<MultipartFile> files);
}
