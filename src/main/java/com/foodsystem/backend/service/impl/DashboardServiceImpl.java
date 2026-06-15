package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.dto.DashboardDto;
import com.foodsystem.backend.repository.OrderRepository;
import com.foodsystem.backend.repository.UserRepository;
import com.foodsystem.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardDto getDashboardStats() {
        Long totalUsers = userRepository.count();
        Long totalOrders = orderRepository.count();
        
        Double totalRevenue = orderRepository.calculateTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        Long pendingOrders = orderRepository.countPendingOrders();
        if (pendingOrders == null) {
            pendingOrders = 0L;
        }

        return DashboardDto.builder()
                .totalUsers(totalUsers)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .pendingOrders(pendingOrders)
                .build();
    }
}
