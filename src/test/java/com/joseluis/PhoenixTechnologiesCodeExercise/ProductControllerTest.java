package com.joseluis.PhoenixTechnologiesCodeExercise;

import com.joseluis.PhoenixTechnologiesCodeExercise.controller.ProductController;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.ProductDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.dto.ProductRequestDTO;
import com.joseluis.PhoenixTechnologiesCodeExercise.exception.EntityNotFoundException;
import com.joseluis.PhoenixTechnologiesCodeExercise.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;

import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.*;
import org.springframework.dao.OptimisticLockingFailureException;
import com.fasterxml.jackson.databind.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductDTO validProductDTO;
    private ProductRequestDTO validProductRequestDTO;

    @BeforeEach
    void setUp() {

        validProductDTO = new ProductDTO();
        validProductDTO.setId(1L);
        validProductDTO.setName("Premium Coffee");
        validProductDTO.setDescription("Arabica whole beans");
        validProductDTO.setPrice(BigDecimal.valueOf(12.99));
        validProductDTO.setQuantity(100);
        validProductDTO.setCategoryId(2L);
        validProductDTO.setCategoryName("Beverages");
        validProductDTO.setVersion(1);
        validProductDTO.setCreatedAt(LocalDateTime.now());
        validProductDTO.setUpdatedAt(LocalDateTime.now());


        validProductRequestDTO = new ProductRequestDTO();
        validProductRequestDTO.setName("Premium Coffee");
        validProductRequestDTO.setDescription("Arabica whole beans");
        validProductRequestDTO.setPrice(BigDecimal.valueOf(12.99));
        validProductRequestDTO.setQuantity(100);
        validProductRequestDTO.setCategoryId(2L);
    }

    @Test
    @DisplayName("POST /api/products - Should create product and return 201")
    void createProduct_WhenValidRequest_ReturnsCreated() throws Exception {

        when(productService.createProduct(any(ProductRequestDTO.class)))
                .thenReturn(validProductDTO);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Premium Coffee"))
                .andExpect(jsonPath("$.price").value(12.99))
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    @DisplayName("GET /api/products - Should return paginated products with 200")
    void getAllProducts_WhenExists_ReturnsPage() throws Exception {

        Page<ProductDTO> page = new PageImpl<>(List.of(validProductDTO));
        when(productService.getAllProducts(any(String.class), any(BigDecimal.class), any(BigDecimal.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/products")
                        .param("name", "Premium Coffee")
                        .param("minPrice", "10")
                        .param("maxPrice", "20")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Premium Coffee"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return product with 200")
    void getProductById_WhenExists_ReturnsProduct() throws Exception {

        when(productService.getProductById(1L)).thenReturn(validProductDTO);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Premium Coffee"))
                .andExpect(jsonPath("$.categoryName").value("Beverages"));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Should update product and return 200")
    void updateProduct_WhenValidRequest_ReturnsUpdatedProduct() throws Exception {

        when(productService.updateProduct(eq(1L), any(ProductRequestDTO.class)))
                .thenReturn(validProductDTO);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Premium Coffee"))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Should delete product and return 204")
    void deleteProduct_WhenExists_ReturnsNoContent() throws Exception {

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/products - Should return 400 when invalid input")
    void createProduct_WhenInvalidRequest_ReturnsBadRequest() throws Exception {

        ProductRequestDTO invalidRequest = new ProductRequestDTO();
        invalidRequest.setName("");
        invalidRequest.setPrice(BigDecimal.valueOf(-1));
        invalidRequest.setQuantity(-5);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return 404 when not found")
    void getProductById_WhenNotExists_ReturnsNotFound() throws Exception {

        when(productService.getProductById(99L))
                .thenThrow(new EntityNotFoundException("Product not found with id: 99"));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Should return 404 when not found")
    void updateProduct_WhenNotExists_ReturnsNotFound() throws Exception {

        when(productService.updateProduct(eq(99L), any(ProductRequestDTO.class)))
                .thenThrow(new EntityNotFoundException("Product not found with id: 99"));

        mockMvc.perform(put("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Should return 409 when version conflict")
    void updateProduct_WhenVersionConflict_ReturnsConflict() throws Exception {

        when(productService.updateProduct(eq(1L), any(ProductRequestDTO.class)))
                .thenThrow(new OptimisticLockingFailureException("Version conflict"));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequestDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Should return 404 when not found")
    void deleteProduct_WhenNotExists_ReturnsNotFound() throws Exception {

        doThrow(new EntityNotFoundException("Product not found with id: 99"))
                .when(productService).deleteProduct(99L);

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/products - Should return 400 when invalid pagination")
    void getAllProducts_WhenInvalidPagination_ReturnsBadRequest() throws Exception {

        mockMvc.perform(get("/api/products")
                        .param("page", "a")
                        .param("size", "1001"))
                .andExpect(status().isBadRequest());
    }
}