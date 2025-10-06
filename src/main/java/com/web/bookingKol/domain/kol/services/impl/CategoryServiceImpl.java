package com.web.bookingKol.domain.kol.services.impl;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.models.Category;
import com.web.bookingKol.domain.kol.repositories.CategoryRepository;
import com.web.bookingKol.domain.kol.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestClient.Builder builder;

    @Override
    public ApiResponse<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ApiResponse.<List<Category>>builder()
                .status(200)
                .message(List.of("Get all categories success"))
                .data(categories)
                .build();
    }

    @Override
    public ApiResponse<Category> findByCategoryId(UUID categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        return ApiResponse.<Category>builder()
                .status(200)
                .message(List.of("Get category success"))
                .data(category)
                .build();
    }

    @Transactional
    public ApiResponse<Category> updateCategory(Category category) {
        if (category.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + category.getId()));
        if (category.getKey() != null && !category.getKey().isBlank()) {
            existingCategory.setKey(category.getKey());
        }
        if (category.getName() != null && !category.getName().isBlank()) {
            existingCategory.setName(category.getName());
        }

        Category saved = categoryRepository.save(existingCategory);
        return ApiResponse.<Category>builder()
                .status(200)
                .message(java.util.List.of("Update category success"))
                .data(saved)
                .build();
    }

    @Transactional
    public ApiResponse<Category> createCategory(Category category) {
        return ApiResponse.<Category>builder()
                .status(200)
                .message(List.of("Update category success"))
                .data(categoryRepository.save(category))
                .build();
    }

    @Transactional
    public String deleteCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {

            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
        return "Delete category success";
    }
}