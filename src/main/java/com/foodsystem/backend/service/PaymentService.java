package com.foodsystem.backend.service;

import com.foodsystem.backend.dto.PaymentDto;

public interface PaymentService {
    PaymentDto processPayment(PaymentDto paymentDto);
    PaymentDto getPaymentByOrderId(Long orderId);
}
