package com.foodsystem.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
