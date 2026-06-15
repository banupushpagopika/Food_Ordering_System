package com.foodsystem.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReportDto {
    private String period;
    private Double totalSales;
    private Long totalOrders;
    private Double averageOrderValue;
}
