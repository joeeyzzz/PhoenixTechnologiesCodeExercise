package com.joseluis.PhoenixTechnologiesCodeExercise.service;

import com.joseluis.PhoenixTechnologiesCodeExercise.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO request);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
}