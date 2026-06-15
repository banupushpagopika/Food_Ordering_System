package com.foodsystem.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDto {
    private Long totalUsers;
    private Long totalOrders;
    private Double totalRevenue;
    private Long pendingOrders;
}
