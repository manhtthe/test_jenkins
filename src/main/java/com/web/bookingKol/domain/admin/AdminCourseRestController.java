package com.web.bookingKol.domain.admin;

import com.web.bookingKol.domain.course.dtos.CoursePackageDTO;
import com.web.bookingKol.domain.course.dtos.UpdateCoursePackageDTO;
import com.web.bookingKol.domain.course.services.CoursePackageService;
import com.web.bookingKol.domain.user.models.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin/course")
public class AdminCourseRestController {
    @Autowired
    private CoursePackageService coursePackageService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCoursesAdmin() {
        return ResponseEntity.ok(coursePackageService.getAllCoursesAdmin());
    }

    @GetMapping("/detail/{courseId}")
    public ResponseEntity<?> getCourseAdmin(@PathVariable("courseId") UUID courseId) {
        return ResponseEntity.ok(coursePackageService.getDetailCourseAdmin(courseId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCourseAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestPart(value = "courseMedias", required = false) List<MultipartFile> courseMedias,
                                               @RequestPart CoursePackageDTO coursePackageDTO) {
        UUID adminId = userDetails.getId();
        return ResponseEntity.ok(coursePackageService.createCoursePackage(adminId, coursePackageDTO, courseMedias));
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<?> updateCourseAdmin(@PathVariable("courseId") UUID courseId,
                                               @RequestBody UpdateCoursePackageDTO updateCoursePackageDTO) {
        return ResponseEntity.ok(coursePackageService.updateCoursePackage(courseId, updateCoursePackageDTO));
    }

    @PostMapping("/medias/upload/{courseId}")
    public ResponseEntity<?> uploadCourseMediaFiles(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable("courseId") UUID courseId,
                                                    @RequestPart List<MultipartFile> files) {
        UUID adminId = userDetails.getId();
        return ResponseEntity.ok(coursePackageService.uploadCourseMediaFiles(adminId, courseId, files));
    }

    @PutMapping("/medias/remove/{courseId}")
    public ResponseEntity<?> removeCourseMediaFile(@PathVariable("courseId") UUID courseId,
                                                   @RequestParam List<UUID> fileUsageIds) {
        return ResponseEntity.ok(coursePackageService.removeCourseMediaFile(courseId, fileUsageIds));
    }

    @PutMapping("/cover-image/set/{courseId}")
    public ResponseEntity<?> setCoverImage(@PathVariable("courseId") UUID courseId,
                                           @RequestParam UUID fileId) {
        return ResponseEntity.ok(coursePackageService.setCoverImage(courseId, fileId));
    }
}
