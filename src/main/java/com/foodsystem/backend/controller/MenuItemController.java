package com.foodsystem.backend.controller;

import com.foodsystem.backend.dto.MenuItemDto;
import com.foodsystem.backend.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemDto> createMenuItem(@Valid @RequestBody MenuItemDto menuItemDto) {
        MenuItemDto created = menuItemService.createMenuItem(menuItemDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> getMenuItemById(@PathVariable Long id) {
        MenuItemDto menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDto>> getAllMenuItems() {
        List<MenuItemDto> list = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItemDto> list = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        List<MenuItemDto> list = menuItemService.getMenuItemsByCategory(categoryId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long id, 
                                                      @Valid @RequestBody MenuItemDto menuItemDto) {
        MenuItemDto updated = menuItemService.updateMenuItem(id, menuItemDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok("Menu item deleted successfully");
    }
}
