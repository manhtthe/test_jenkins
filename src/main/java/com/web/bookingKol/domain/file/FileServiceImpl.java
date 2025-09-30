package com.web.bookingKol.domain.file;

import com.web.bookingKol.common.Enums;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.file.dtos.FileDTO;
import com.web.bookingKol.domain.file.mappers.FileMapper;
import com.web.bookingKol.domain.file.models.File;
import com.web.bookingKol.domain.file.repositories.FileRepository;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private SupabaseStorageService supabaseStorageService;
    @Autowired
    private FileValidator fileValidator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileRepository fileRepository;

    @Transactional
    @Override
    public ApiResponse<FileDTO> uploadFile(UUID uploaderId, MultipartFile file) {
        if (file == null || file.getSize() <= 0) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        File fileUploading = new File();
        if (fileValidator.isImage(file)) {
            fileUploading.setFileType(Enums.FileType.IMAGE.name());
        } else if (fileValidator.isVideo(file)) {
            fileUploading.setFileType(Enums.FileType.VIDEO.name());
        } else if (fileValidator.isDocument(file)) {
            fileUploading.setFileType(Enums.FileType.DOCUMENT.name());
        } else {
            throw new IllegalArgumentException("Unsupported file type");
        }
        UUID fileId = UUID.randomUUID();
        fileUploading.setId(fileId);
        User uploader = userRepository.findById(uploaderId).orElseThrow(() -> new IllegalArgumentException("Uploader not found"));
        fileUploading.setUploader(uploader);
        fileUploading.setStatus(Enums.FileStatus.ACTIVE.name());

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = extension == null || extension.isEmpty()
                ? fileId.toString()
                : fileId + "." + extension.toLowerCase();
        fileUploading.setFileName(fileName);

        String fileUrl = supabaseStorageService.uploadFile(file, fileName, file.getContentType());
        fileUploading.setFileUrl(fileUrl);

        fileUploading.setSizeBytes(file.getSize());
        fileUploading.setCreatedAt(Instant.now());
        fileRepository.save(fileUploading);
        return ApiResponse.<FileDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("File uploaded successfully"))
                .data(fileMapper.toDto(fileUploading))
                .build();

    }
}
