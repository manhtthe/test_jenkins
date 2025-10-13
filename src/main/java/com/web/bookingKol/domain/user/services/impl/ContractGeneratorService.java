package com.web.bookingKol.domain.user.services.impl;

import com.web.bookingKol.common.Enums;
import com.web.bookingKol.domain.file.dtos.FileDTO;
import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import com.web.bookingKol.domain.file.mappers.FileMapper;
import com.web.bookingKol.domain.file.mappers.FileUsageMapper;
import com.web.bookingKol.domain.file.models.File;
import com.web.bookingKol.domain.file.services.FileService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
public class ContractGeneratorService {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileUsageMapper fileUsageMapper;
    public FileUsageDTO generateAndSaveContract(Map<String, String> placeholders, UUID uploaderId, UUID contractId) {
        try {
            String templatePath = "src/main/resources/templates/contract_template.docx";
            String uploadDir = "uploads/contracts/" + LocalDate.now();
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = "contract_" + contractId + ".docx";
            Path filePath = Paths.get(uploadDir, fileName);

            try (FileInputStream fis = new FileInputStream(templatePath);
                 XWPFDocument document = new XWPFDocument(fis)) {

                for (XWPFParagraph p : document.getParagraphs()) {
                    for (XWPFRun r : p.getRuns()) {
                        String text = r.getText(0);
                        if (text != null) {
                            for (var entry : placeholders.entrySet()) {
                                text = text.replace("${" + entry.getKey() + "}", entry.getValue());
                            }
                            r.setText(text, 0);
                        }
                    }
                }

                try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                    document.write(fos);
                }
            }

            java.io.File physicalFile = filePath.toFile();
            long size = physicalFile.length();

            File fileEntity = new File();
            fileEntity.setId(UUID.randomUUID());
            fileEntity.setFileName(fileName);
            fileEntity.setFileUrl(filePath.toString());
            fileEntity.setFileType("DOCX");
            fileEntity.setSizeBytes(size);
            fileEntity.setStatus("ACTIVE");
            fileEntity.setCreatedAt(java.time.Instant.now());

            FileDTO fileDTO = fileService.uploadFilePoint(uploaderId, toMultipartFile(physicalFile, fileName));

            // 4️⃣ Lưu FileUsage liên kết CONTRACT
            FileUsageDTO fileUsageDTO = fileService.createFileUsage(
                    fileMapper.toEntity(fileDTO),
                    contractId,
                    Enums.TargetType.CONTRACT.name(),
                    false
            );

            return fileUsageDTO;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi sinh hợp đồng và lưu DB: " + e.getMessage(), e);
        }
    }

    private MultipartFile toMultipartFile(java.io.File file, String fileName) {
        return new MultipartFile() {
            @Override public String getName() { return fileName; }
            @Override public String getOriginalFilename() { return fileName; }
            @Override public String getContentType() { return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"; }
            @Override public boolean isEmpty() { return file.length() == 0; }
            @Override public long getSize() { return file.length(); }
            @Override public byte[] getBytes() throws IOException { return Files.readAllBytes(file.toPath()); }
            @Override public InputStream getInputStream() throws IOException { return new FileInputStream(file); }
            @Override public void transferTo(java.io.File dest) throws IOException { Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING); }
        };
    }
}
