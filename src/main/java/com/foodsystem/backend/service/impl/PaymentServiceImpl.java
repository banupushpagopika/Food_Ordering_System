package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.dto.PaymentDto;
import com.foodsystem.backend.entity.Order;
import com.foodsystem.backend.entity.Payment;
import com.foodsystem.backend.entity.PaymentStatus;
import com.foodsystem.backend.exception.BadRequestException;
import com.foodsystem.backend.exception.ResourceNotFoundException;
import com.foodsystem.backend.repository.OrderRepository;
import com.foodsystem.backend.repository.PaymentRepository;
import com.foodsystem.backend.service.PaymentService;
import com.foodsystem.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public PaymentDto processPayment(PaymentDto paymentDto) {
        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + paymentDto.getOrderId()));

        Payment payment = order.getPayment();
        if (payment == null) {
            throw new ResourceNotFoundException("Payment details not found for Order ID: " + paymentDto.getOrderId());
        }

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new BadRequestException("Payment is already completed for this order.");
        }

        payment.setPaymentStatus(paymentDto.getPaymentStatus());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());

        Payment savedPayment = paymentRepository.save(payment);

        // Notify
        notificationService.sendPaymentNotification(savedPayment, 
                "Payment status updated to: " + savedPayment.getPaymentStatus() + " via " + savedPayment.getPaymentMethod());

        return mapToDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        Payment payment = order.getPayment();
        if (payment == null) {
            throw new ResourceNotFoundException("Payment details not found for Order ID: " + orderId);
        }

        return mapToDto(payment);
    }

    private PaymentDto mapToDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }
}
