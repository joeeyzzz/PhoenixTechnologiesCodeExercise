package com.joseluis.PhoenixTechnologiesCodeExercise.controller;

import com.joseluis.PhoenixTechnologiesCodeExercise.dto.CategoryDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.CategoryRequestDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management APIs")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category")
    public CategoryDTO createCategory(@Valid @RequestBody CategoryRequestDTO request) {

        return categoryService.createCategory(request);
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public CategoryDTO getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
}