package com.web.bookingKol.domain.kol.rest;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.models.Category;
import com.web.bookingKol.domain.kol.services.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','KOL')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }



    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','KOL')")
    @GetMapping("/{id}")
    public ResponseEntity<Category>  getCategories(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(categoryService.findByCategoryId(id).getData());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable("id") UUID id,
            @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}