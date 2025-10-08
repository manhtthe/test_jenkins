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
    public ApiResponse<Category> updateCategory(Category category) {
        if (category.getId() == null) {
            throw new IllegalArgumentException("Id bắt buộc");
        }

        Category existing = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new RuntimeException("không tìm thấy: " + category.getId()));

        if (category.getKey() != null && !category.getKey().isBlank()) {
            existing.setKey(category.getKey());
        }
        if (category.getName() != null && !category.getName().isBlank()) {
            existing.setName(category.getName());
        }

        Category saved = categoryRepository.save(existing);
        return ApiResponse.<Category>builder()
                .status(200)
                .message(List.of("cập nhật category thành công"))
                .data(saved)
                .build();
    }

    @Transactional
    public ApiResponse<Category> createCategory(Category category) {
        return ApiResponse.<Category>builder()
                .status(200)
                .message(List.of("tạo category thành công"))
                .data(categoryRepository.save(category))
                .build();
    }

    @Transactional
    public String deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category id: " + categoryId));
        boolean newStatus = !category.isDeleted();
        category.setDeleted(newStatus);
        categoryRepository.save(category);

        return newStatus
                ? "Đã chuyển category sang trạng thái đã xóa "
                : "Đã khôi phục category";
    }

}