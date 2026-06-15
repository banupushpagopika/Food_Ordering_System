package com.foodsystem.backend.service;

import com.foodsystem.backend.dto.OrderDto;
import com.foodsystem.backend.dto.OrderResponseDto;
import com.foodsystem.backend.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(String email, OrderDto orderDto);
    OrderResponseDto getOrderById(Long orderId, String email);
    List<OrderResponseDto> getMyOrders(String email);
    List<OrderResponseDto> getAllOrders();
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status);
    void deleteOrder(Long orderId);
}
