package com.joseluis.PhoenixTechnologiesCodeExercise.service;

import com.joseluis.PhoenixTechnologiesCodeExercise.dto.CategoryDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.DTOMapper;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.ProductDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.ProductRequestDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.exception.EntityNotFoundException;
import com.joseluis.PhoenixTechnologiesCodeExercise.model.Product;
import com.joseluis.PhoenixTechnologiesCodeExercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final DTOMapper dtoMapper;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductRequestDTO request) {

        Product product = dtoMapper.toProductEntity(request);
        setCategoryIfPresent(product, request.getCategoryId());
        Product savedProduct = productRepository.save(product);
        return dtoMapper.toProductDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        Specification<Product> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (minPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        return productRepository.findAll(spec, pageable)
                .map(dtoMapper::toProductDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {

        Product product = getExistingProduct(id);
        return dtoMapper.toProductDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductRequestDTO request) {

        Product existingProduct = getExistingProduct(id);
        dtoMapper.updateProductFromDTO(request, existingProduct);
        setCategoryIfPresent(existingProduct, request.getCategoryId());
        Product updatedProduct = productRepository.save(existingProduct);
        return dtoMapper.toProductDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {

            throw new EntityNotFoundException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    private Product getExistingProduct(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    private void setCategoryIfPresent(Product product, Long categoryId) {

        if (categoryId != null) {

            CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
            product.setCategory(dtoMapper.toCategoryEntity(categoryDTO));
        }
    }
}