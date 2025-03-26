package com.joseluis.PhoenixTechnologiesCodeExercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;
}