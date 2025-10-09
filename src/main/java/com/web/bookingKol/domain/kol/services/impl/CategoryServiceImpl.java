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
                .message(List.of("Lấy tất cả categories thành công"))
                .data(categories)
                .build();
    }

    @Override
    public ApiResponse<Category> findByCategoryId(UUID categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("không tìm thấy category id: " + categoryId));
        return ApiResponse.<Category>builder()
                .status(200)
                .message(List.of("Lấy category thành công"))
                .data(category)
                .build();
    }

    @Transactional
    public ApiResponse<Category> updateCategory(UUID id, Category updateRequest) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category id: " + id));

        if (updateRequest.getName() != null && !updateRequest.getName().isBlank()) {
            existing.setName(updateRequest.getName());
        }
        if (updateRequest.getKey() != null && !updateRequest.getKey().isBlank()) {
            existing.setKey(updateRequest.getKey());
        }

        Category saved = categoryRepository.save(existing);
        return ApiResponse.<Category>builder()
                .status(200)
                .message(List.of("Cập nhật category thành công"))
                .data(saved)
                .build();
    }

    @Transactional
    public ApiResponse<Category> createCategory(Category category) {
        category.setId(null);
        category.setDeleted(false);

        Category saved = categoryRepository.save(category);
        return ApiResponse.<Category>builder()
                .status(201)
                .message(List.of("Tạo category thành công"))
                .data(saved)
                .build();
    }

    @Transactional
    public ApiResponse<String> deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category id: " + categoryId));

        boolean newStatus = !category.isDeleted();
        category.setDeleted(newStatus);
        categoryRepository.save(category);

        String message = newStatus
                ? "Đã chuyển category sang trạng thái đã xóa"
                : "Đã khôi phục category";

        return ApiResponse.<String>builder()
                .status(200)
                .message(List.of(message))
                .data(message)
                .build();
    }

}