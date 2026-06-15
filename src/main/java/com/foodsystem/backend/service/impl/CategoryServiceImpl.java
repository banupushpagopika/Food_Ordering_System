package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.dto.CategoryDto;
import com.foodsystem.backend.entity.Category;
import com.foodsystem.backend.exception.BadRequestException;
import com.foodsystem.backend.exception.ResourceNotFoundException;
import com.foodsystem.backend.repository.CategoryRepository;
import com.foodsystem.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByCategoryName(categoryDto.getCategoryName()).isPresent()) {
            throw new BadRequestException("Category already exists with name: " + categoryDto.getCategoryName());
        }
        Category category = mapToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return mapToDto(savedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        categoryRepository.findByCategoryName(categoryDto.getCategoryName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new BadRequestException("Another category already exists with name: " + categoryDto.getCategoryName());
                    }
                });

        category.setCategoryName(categoryDto.getCategoryName());
        category.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return mapToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    private Category mapToEntity(CategoryDto dto) {
        return Category.builder()
                .id(dto.getId())
                .categoryName(dto.getCategoryName())
                .description(dto.getDescription())
                .build();
    }

    private CategoryDto mapToDto(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .categoryName(entity.getCategoryName())
                .description(entity.getDescription())
                .build();
    }
}
