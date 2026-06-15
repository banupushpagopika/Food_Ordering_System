package com.foodsystem.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDto {

    private Long id;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Item description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Availability status is required")
    private Boolean availability;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String categoryName;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    private String restaurantName;
}
