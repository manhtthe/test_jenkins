package com.web.bookingKol.domain.course;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.course.services.CoursePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CoursePackageRestController {
    @Autowired
    private CoursePackageService coursePackageService;

    @GetMapping("/all")
    ResponseEntity<ApiResponse<?>> getAllCourses(@RequestParam(required = false) Integer minPrice,
                                                 @RequestParam(required = false) Integer maxPrice,
                                                 @RequestParam(required = false) Integer minDiscount,
                                                 @RequestParam(required = false) Integer maxDiscount,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "price") String sortBy,
                                                 @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok().body(coursePackageService.getAllCourse(
                minPrice, maxPrice, minDiscount, maxDiscount, page, size, sortBy, sortDir
        ));
    }

    @GetMapping("/{coursePackageId}")
    ResponseEntity<ApiResponse<?>> getCourseById(@PathVariable UUID coursePackageId) {
        return ResponseEntity.ok().body(coursePackageService.getCoursePackageById(coursePackageId));
    }
}
