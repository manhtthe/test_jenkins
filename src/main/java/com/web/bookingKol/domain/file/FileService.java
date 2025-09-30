package com.web.bookingKol.domain.file;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.file.dtos.FileDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public interface FileService {
    ApiResponse<FileDTO> uploadFile(UUID uploaderId, MultipartFile file);
}
