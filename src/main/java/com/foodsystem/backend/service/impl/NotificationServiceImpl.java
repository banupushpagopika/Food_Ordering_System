package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.entity.Order;
import com.foodsystem.backend.entity.Payment;
import com.foodsystem.backend.entity.User;
import com.foodsystem.backend.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendOrderNotification(Order order, String message) {
        log.info("NOTIFICATION [ORDER #{} for {}]: {}", 
                order.getId(), 
                order.getUser().getName(), 
                message);
    }

    @Override
    public void sendPaymentNotification(Payment payment, String message) {
        log.info("NOTIFICATION [PAYMENT #{} for ORDER #{}]: {}", 
                payment.getId(), 
                payment.getOrder().getId(), 
                message);
    }

    @Override
    public void sendUserRegistrationNotification(User user) {
        log.info("NOTIFICATION [USER REGISTRATION]: Welcome, {}! Role: {}, Email: {}", 
                user.getName(), 
                user.getRole(), 
                user.getEmail());
    }
}
