package com.joseluis.PhoenixTechnologiesCodeExercise.service;

import com.joseluis.PhoenixTechnologiesCodeExercise.dto.CategoryDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.ProductDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.ProductRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;


public interface ProductService {

    ProductDTO createProduct(ProductRequestDTO request);
    Page<ProductDTO> getAllProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Long id, ProductRequestDTO request);
    void deleteProduct(Long id);
}