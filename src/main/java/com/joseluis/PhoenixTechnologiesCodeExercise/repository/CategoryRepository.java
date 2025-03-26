package com.joseluis.PhoenixTechnologiesCodeExercise.repository;

import com.joseluis.PhoenixTechnologiesCodeExercise.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {}