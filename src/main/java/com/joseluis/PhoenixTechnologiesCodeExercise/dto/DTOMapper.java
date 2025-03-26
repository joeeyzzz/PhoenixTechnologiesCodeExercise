package com.joseluis.PhoenixTechnologiesCodeExercise.dto;

import com.joseluis.PhoenixTechnologiesCodeExercise.model.Category;
import com.joseluis.PhoenixTechnologiesCodeExercise.model.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DTOMapper {

    private final ModelMapper modelMapper;

    public DTOMapper() {

        this.modelMapper = new ModelMapper();
        configureMappings();
    }

    private void configureMappings() {

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        modelMapper.typeMap(ProductRequestDTO.class, Product.class)
                .addMappings(mapper -> mapper.skip(Product::setId));
    }

    public Product toProductEntity(ProductRequestDTO dto) {

        return modelMapper.map(dto, Product.class);
    }

    public ProductDTO toProductDTO(Product entity) {

        ProductDTO dto = modelMapper.map(entity, ProductDTO.class);

        if (entity.getCategory() != null) {

            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }

        return dto;
    }

    public void updateProductFromDTO(ProductRequestDTO dto, Product entity) {

        modelMapper.map(dto, entity);
    }

    public Category toCategoryEntity(CategoryDTO dto) {

        return modelMapper.map(dto, Category.class);
    }

    public CategoryDTO toCategoryDTO(Category entity) {

        CategoryDTO dto = modelMapper.map(entity, CategoryDTO.class);

        dto.setProducts(entity.getProducts().stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList()));

        return dto;
    }
}