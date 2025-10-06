package com.web.bookingKol.domain.kol.rest;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.models.Category;
import com.web.bookingKol.domain.kol.services.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Category>>  getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories().getData());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category>  getCategories(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(categoryService.findByCategoryId(id).getData());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category>  updateCategories(@PathVariable("id") UUID id, Category category) {
        category.setId(id);
        return ResponseEntity.ok(categoryService.updateCategory(category).getData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  deleteCategories(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}