package com.joseluis.PhoenixTechnologiesCodeExercise.controller;

import com.joseluis.PhoenixTechnologiesCodeExercise.dto.ProductDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.ProductRequestDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new product")
    public ProductDTO createProduct(@Valid @RequestBody ProductRequestDTO request) {

        return productService.createProduct(request);
    }

    @GetMapping
    @Operation(summary = "Get filtered and paginated products")
    public Page<ProductDTO> getAllProducts(

            @Parameter(description = "Filter by product name (partial match)")
            @RequestParam(required = false) String name,

            @Parameter(description = "Minimum price filter")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Maximum price filter")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Page number (0..N)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Items per page")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sorting criteria")
            @RequestParam(defaultValue = "name,asc") String sort
    ) {

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = (sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1]))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return productService.getAllProducts(name, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ProductDTO getProductById(@PathVariable Long id) {

        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {

        try {
            return ResponseEntity.ok(productService.updateProduct(id, request));
        } catch (OptimisticLockingFailureException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Version conflict");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product")
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
    }
}