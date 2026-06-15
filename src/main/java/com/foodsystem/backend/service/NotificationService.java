package com.foodsystem.backend.service;

import com.foodsystem.backend.entity.Order;
import com.foodsystem.backend.entity.Payment;
import com.foodsystem.backend.entity.User;

public interface NotificationService {
    void sendOrderNotification(Order order, String message);
    void sendPaymentNotification(Payment payment, String message);
    void sendUserRegistrationNotification(User user);
}
