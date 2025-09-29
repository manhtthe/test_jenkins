package com.web.bookingKol.domain.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class FileValidator {
    private static final Set<String> DOCUMENT_MIME_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "text/plain"
    );

    public boolean isVideo(MultipartFile file) {
        String mimeType = file.getContentType();
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("video/");
    }

    public boolean isImage(MultipartFile file) {
        String mimeType = file.getContentType();
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("image/");
    }

    public boolean isDocument(MultipartFile file) {
        String mimeType = file.getContentType();
        return mimeType != null && DOCUMENT_MIME_TYPES.contains(mimeType);
    }
}
