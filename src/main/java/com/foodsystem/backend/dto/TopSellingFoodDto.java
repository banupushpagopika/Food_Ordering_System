package com.foodsystem.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopSellingFoodDto {
    private Long menuItemId;
    private String itemName;
    private Long totalQuantity;
    private Double totalRevenue;
}
