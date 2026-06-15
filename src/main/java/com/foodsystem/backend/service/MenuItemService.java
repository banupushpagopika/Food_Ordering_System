package com.foodsystem.backend.service;

import com.foodsystem.backend.dto.MenuItemDto;

import java.util.List;

public interface MenuItemService {
    MenuItemDto createMenuItem(MenuItemDto menuItemDto);
    MenuItemDto getMenuItemById(Long id);
    List<MenuItemDto> getAllMenuItems();
    List<MenuItemDto> getMenuItemsByRestaurant(Long restaurantId);
    List<MenuItemDto> getMenuItemsByCategory(Long categoryId);
    MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto);
    void deleteMenuItem(Long id);
}
