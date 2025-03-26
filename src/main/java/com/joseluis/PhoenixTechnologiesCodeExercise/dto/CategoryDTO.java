package com.joseluis.PhoenixTechnologiesCodeExercise.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<ProductDTO> products;
}