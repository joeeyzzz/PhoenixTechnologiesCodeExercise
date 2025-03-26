package com.joseluis.PhoenixTechnologiesCodeExercise.service;

import com.joseluis.PhoenixTechnologiesCodeExercise.dto.CategoryDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.CategoryRequestDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryRequestDTO request);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
}