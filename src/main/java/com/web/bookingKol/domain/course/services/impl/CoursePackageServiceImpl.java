package com.web.bookingKol.domain.course.services.impl;

import com.web.bookingKol.common.Enums;
import com.web.bookingKol.common.UpdateEntityUtil;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.course.CoursePackage;
import com.web.bookingKol.domain.course.CoursePackageMapper;
import com.web.bookingKol.domain.course.CoursePackageRepository;
import com.web.bookingKol.domain.course.dtos.CoursePackageDTO;
import com.web.bookingKol.domain.course.dtos.UpdateCoursePackageDTO;
import com.web.bookingKol.domain.course.services.CoursePackageService;
import com.web.bookingKol.domain.file.FileService;
import com.web.bookingKol.domain.file.dtos.FileDTO;
import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import com.web.bookingKol.domain.file.mappers.FileMapper;
import com.web.bookingKol.domain.file.mappers.FileUsageMapper;
import com.web.bookingKol.domain.file.models.File;
import com.web.bookingKol.domain.file.models.FileUsage;
import com.web.bookingKol.domain.file.repositories.FileRepository;
import com.web.bookingKol.domain.file.repositories.FileUsageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class CoursePackageServiceImpl implements CoursePackageService {
    @Autowired
    private CoursePackageRepository coursePackageRepository;
    @Autowired
    private CoursePackageMapper coursePackageMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileUsageMapper fileUsageMapper;
    @Autowired
    private FileUsageRepository fileUsageRepository;

    @Override
    public ApiResponse<CoursePackageDTO> getCoursePackageById(UUID coursePackageId) {
        CoursePackage coursePackage = coursePackageRepository.findByCoursePackageId(coursePackageId, Enums.TargetType.COURSE_PACKAGE.name())
                .orElseThrow(() -> new RuntimeException("CoursePackage not found for id: " + coursePackageId));
        return ApiResponse.<CoursePackageDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get course package by coursePackageId success"))
                .data(coursePackageMapper.toDto(coursePackage))
                .build();
    }

    @Override
    public ApiResponse<List<CoursePackageDTO>> getAllCourse() {
        List<CoursePackageDTO> coursePackages = coursePackageMapper.toDtoList(coursePackageRepository.findAll()
                .stream().filter(coursePackage -> Boolean.TRUE.equals(coursePackage.getIsAvailable())).toList());
        return ApiResponse.<List<CoursePackageDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get all course available packages success"))
                .data(coursePackages)
                .build();
    }

    @Override
    public ApiResponse<List<CoursePackageDTO>> getAllCoursesAdmin() {
        List<CoursePackageDTO> coursePackages = coursePackageMapper.toDtoList(coursePackageRepository.findAll());
        return ApiResponse.<List<CoursePackageDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get all course available packages success"))
                .data(coursePackages)
                .build();
    }

    @Override
    public ApiResponse<CoursePackageDTO> getDetailCourseAdmin(UUID coursePackageId) {
        CoursePackage coursePackage = coursePackageRepository.findById(coursePackageId)
                .orElseThrow(() -> new RuntimeException("CoursePackage not found for id: " + coursePackageId));
        return ApiResponse.<CoursePackageDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Get course package by coursePackageId success"))
                .data(coursePackageMapper.toDto(coursePackage))
                .build();
    }

    @Transactional
    @Override
    public ApiResponse<CoursePackageDTO> createCoursePackage(UUID adminId, CoursePackageDTO coursePackageDTO, List<MultipartFile> courseMedias) {
        CoursePackage cp = new CoursePackage();
        cp.setId(UUID.randomUUID());
        cp.setName(coursePackageDTO.getName());
        cp.setPrice(coursePackageDTO.getPrice());
        cp.setDiscount(coursePackageDTO.getDiscount());
        cp.setDescription(coursePackageDTO.getDescription());
        cp.setIsAvailable(true);
        Set<FileUsage> imageFiles = new LinkedHashSet<>();
        if (courseMedias != null && !courseMedias.isEmpty()) {
            courseMedias.forEach(image -> {
                FileDTO fileDTO = fileService.uploadFilePoint(adminId, image);
                FileUsageDTO fileUsageDTO = fileService.createFileUsage(fileMapper.toEntity(fileDTO), adminId, Enums.TargetType.COURSE_PACKAGE.name(), false);
                imageFiles.add(fileUsageMapper.toEntity(fileUsageDTO));
            });
            cp.setFileUsages(imageFiles);
        }
        coursePackageRepository.save(cp);
        return ApiResponse.<CoursePackageDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Create Course package successfully!"))
                .data(coursePackageMapper.toDto(cp))
                .build();
    }

    @Transactional
    @Override
    public ApiResponse<CoursePackageDTO> updateCoursePackage(UUID coursePackageId, UpdateCoursePackageDTO updateCoursePackageDTO) {
        CoursePackage cp = coursePackageRepository.findById(coursePackageId)
                .orElseThrow(() -> new EntityNotFoundException("CoursePackage not found for id: " + coursePackageId));
        if (updateCoursePackageDTO != null) {
            BeanUtils.copyProperties(updateCoursePackageDTO, cp, UpdateEntityUtil.getNullPropertyNames(updateCoursePackageDTO));
        }
        coursePackageRepository.save(cp);
        return ApiResponse.<CoursePackageDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Update Course package successfully!"))
                .data(coursePackageMapper.toDto(cp))
                .build();
    }

    @Transactional
    @Override
    public ApiResponse<List<FileUsageDTO>> uploadCourseMediaFiles(UUID uploaderId, UUID courseId, List<MultipartFile> files) {
        CoursePackage cp = coursePackageRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("CoursePackage not found for id: " + courseId));
        if (files != null && !files.isEmpty()) {
            List<FileUsageDTO> fileUsageDTOS = new ArrayList<>();
            for (MultipartFile file : files) {
                FileDTO fileDTO = fileService.uploadFilePoint(uploaderId, file);
                FileUsageDTO fileUsageDTO = fileService.createFileUsage(fileMapper.toEntity(fileDTO), cp.getId(), Enums.TargetType.COURSE_PACKAGE.name(), false);
                fileUsageDTOS.add(fileUsageDTO);
            }
            return ApiResponse.<List<FileUsageDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message(List.of("Upload media files success: " + fileUsageDTOS.size() + " files"))
                    .data(fileUsageDTOS)
                    .build();
        } else {
            return ApiResponse.<List<FileUsageDTO>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("No files to upload"))
                    .data(null)
                    .build();
        }

    }

    @Override
    public ApiResponse<?> removeCourseMediaFile(UUID courseId, List<UUID> fileUsageIds) {
        if (!coursePackageRepository.existsById(courseId)) {
            throw new EntityNotFoundException("CoursePackage not found for id: " + courseId);
        }
        List<FileUsage> fileUsages = fileUsageRepository.findAllById(fileUsageIds).stream()
                .filter(fu -> fu.getTargetType().equals(Enums.TargetType.COURSE_PACKAGE.name())).toList();
        if (fileUsages.size() != fileUsageIds.size()) {
            throw new EntityNotFoundException("Some COURSE_PACKAGE File not found for provided IDs " + fileUsages.size() + "/" + fileUsageIds.size());
        }
        fileUsages.forEach(fileUsage -> {
            fileUsage.setIsActive(false);
            fileUsage.setIsCover(false);
        });
        fileUsageRepository.saveAll(fileUsages);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Successfully remove " + fileUsages.size() + " media files"))
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<FileUsageDTO> setCoverImage(UUID courseId, UUID fileId) {
        CoursePackage cp = coursePackageRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("CoursePackage not found for id: " + courseId));
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found for id: " + fileId));
        if (!file.getFileType().equals(Enums.FileType.IMAGE.name())) {
            throw new IllegalArgumentException("File is not an image for ID: " + fileId);
        }
        if (!file.getStatus().equals(Enums.FileStatus.ACTIVE.name())) {
            throw new IllegalArgumentException("File is not active for ID: " + fileId);
        }
        FileUsage fileUsage = file.getFileUsages().stream()
                .filter(fu -> fu.getTargetId().equals(courseId) && fu.getTargetType().equals(Enums.TargetType.COURSE_PACKAGE.name()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("File is not associated with this Course package Id: " + courseId));
        if (fileUsage.getTargetType().equals(Enums.TargetType.COURSE_PACKAGE.name())) {
            cp.getFileUsages().stream()
                    .filter(fu -> fu.getIsActive() && fu.getIsCover())
                    .findAny()
                    .ifPresent(fu -> {
                        fu.setIsCover(false);
                        fileUsageRepository.save(fu);
                    });
            fileUsage.setIsCover(true);
            fileUsageRepository.save(fileUsage);
        } else {
            throw new IllegalArgumentException("File is not a COURSE_PACKAGE media of this Course package!");
        }
        return ApiResponse.<FileUsageDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Set cover image successfully"))
                .data(fileUsageMapper.toDto(fileUsage))
                .build();
    }
}
