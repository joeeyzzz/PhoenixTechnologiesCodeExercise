package com.joseluis.PhoenixTechnologiesCodeExercise.service;

import com.joseluis.PhoenixTechnologiesCodeExercise.dto.CategoryDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.DTOMapper;
import com.joseluis.PhoenixTechnologiesCodeExercise.exception.EntityNotFoundException;
import com.joseluis.PhoenixTechnologiesCodeExercise.model.Category;
import com.joseluis.PhoenixTechnologiesCodeExercise.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final DTOMapper dtoMapper;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO request) {

        Category category = dtoMapper.toCategoryEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return dtoMapper.toCategoryDTO(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {

        return categoryRepository.findAll().stream()
                .map(dtoMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .map(dtoMapper::toCategoryDTO)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }
}