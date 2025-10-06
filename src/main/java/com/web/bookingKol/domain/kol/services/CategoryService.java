package com.web.bookingKol.domain.kol.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.models.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    ApiResponse<List<Category>> getAllCategories();

    ApiResponse<Category> findByCategoryId(UUID categoryId);
}
