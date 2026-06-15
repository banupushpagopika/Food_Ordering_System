package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.dto.OrderDto;
import com.foodsystem.backend.dto.OrderItemResponseDto;
import com.foodsystem.backend.dto.OrderResponseDto;
import com.foodsystem.backend.entity.*;
import com.foodsystem.backend.exception.BadRequestException;
import com.foodsystem.backend.exception.ResourceNotFoundException;
import com.foodsystem.backend.repository.*;
import com.foodsystem.backend.service.OrderService;
import com.foodsystem.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public OrderResponseDto createOrder(String email, OrderDto orderDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Your cart is empty. Please add items before placing an order.");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(Cart::getTotalPrice)
                .sum();

        // Build Order
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.PENDING)
                .build();

        // Build Order Items
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .menuItem(cartItem.getMenuItem())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getMenuItem().getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // Build Payment
        Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .paymentMethod(orderDto.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        order.setPayment(payment);

        // Save order (cascades order items and payment)
        Order savedOrder = orderRepository.save(order);

        // Clear user's cart
        cartRepository.deleteByUser(user);

        // Send Notification
        notificationService.sendOrderNotification(savedOrder, "Your order has been placed successfully and is pending confirmation.");

        return mapToDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Check permission (Owner or Admin)
        if (user.getRole() != Role.ADMIN && !order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You do not have permission to view this order.");
        }

        return mapToDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        return orders.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setOrderStatus(status);

        // If order is delivered, we can optionally mark payment as completed (e.g. for Cash on Delivery)
        if (status == OrderStatus.DELIVERED && order.getPayment().getPaymentMethod() == PaymentMethod.COD) {
            order.getPayment().setPaymentStatus(PaymentStatus.COMPLETED);
        }

        Order updatedOrder = orderRepository.save(order);

        // Send notification
        notificationService.sendOrderNotification(updatedOrder, "Your order status has been updated to: " + status);

        return mapToDto(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        orderRepository.delete(order);
    }

    private OrderResponseDto mapToDto(Order order) {
        List<OrderItemResponseDto> itemDtos = order.getOrderItems().stream()
                .map(item -> OrderItemResponseDto.builder()
                        .id(item.getId())
                        .menuItemId(item.getMenuItem().getId())
                        .menuItemName(item.getMenuItem().getItemName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userName(order.getUser().getName())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .items(itemDtos)
                .paymentMethod(order.getPayment().getPaymentMethod())
                .paymentStatus(order.getPayment().getPaymentStatus())
                .build();
    }
}
