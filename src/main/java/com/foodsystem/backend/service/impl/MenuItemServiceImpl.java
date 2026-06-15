package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.dto.MenuItemDto;
import com.foodsystem.backend.entity.Category;
import com.foodsystem.backend.entity.MenuItem;
import com.foodsystem.backend.entity.Restaurant;
import com.foodsystem.backend.exception.ResourceNotFoundException;
import com.foodsystem.backend.repository.CategoryRepository;
import com.foodsystem.backend.repository.MenuItemRepository;
import com.foodsystem.backend.repository.RestaurantRepository;
import com.foodsystem.backend.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public MenuItemDto createMenuItem(MenuItemDto menuItemDto) {
        Category category = categoryRepository.findById(menuItemDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + menuItemDto.getCategoryId()));

        Restaurant restaurant = restaurantRepository.findById(menuItemDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + menuItemDto.getRestaurantId()));

        MenuItem menuItem = mapToEntity(menuItemDto, category, restaurant);
        MenuItem savedItem = menuItemRepository.save(menuItem);
        return mapToDto(savedItem);
    }

    @Override
    public MenuItemDto getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        return mapToDto(menuItem);
    }

    @Override
    public List<MenuItemDto> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemDto> getMenuItemsByRestaurant(Long restaurantId) {
        // Verify restaurant exists
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemDto> getMenuItemsByCategory(Long categoryId) {
        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return menuItemRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        Category category = categoryRepository.findById(menuItemDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + menuItemDto.getCategoryId()));

        Restaurant restaurant = restaurantRepository.findById(menuItemDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + menuItemDto.getRestaurantId()));

        menuItem.setItemName(menuItemDto.getItemName());
        menuItem.setDescription(menuItemDto.getDescription());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setAvailability(menuItemDto.getAvailability());
        menuItem.setCategory(category);
        menuItem.setRestaurant(restaurant);

        MenuItem updatedItem = menuItemRepository.save(menuItem);
        return mapToDto(updatedItem);
    }

    @Override
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        menuItemRepository.delete(menuItem);
    }

    private MenuItem mapToEntity(MenuItemDto dto, Category category, Restaurant restaurant) {
        return MenuItem.builder()
                .id(dto.getId())
                .itemName(dto.getItemName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .availability(dto.getAvailability())
                .category(category)
                .restaurant(restaurant)
                .build();
    }

    private MenuItemDto mapToDto(MenuItem entity) {
        return MenuItemDto.builder()
                .id(entity.getId())
                .itemName(entity.getItemName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .availability(entity.getAvailability())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getCategoryName())
                .restaurantId(entity.getRestaurant().getId())
                .restaurantName(entity.getRestaurant().getRestaurantName())
                .build();
    }
}
