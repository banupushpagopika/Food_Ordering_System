package com.foodsystem.backend.dto;

import com.foodsystem.backend.entity.OrderStatus;
import com.foodsystem.backend.entity.PaymentMethod;
import com.foodsystem.backend.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemResponseDto> items;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
}
