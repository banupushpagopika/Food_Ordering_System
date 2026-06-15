package com.foodsystem.backend.controller;

import com.foodsystem.backend.dto.OrderDto;
import com.foodsystem.backend.dto.OrderResponseDto;
import com.foodsystem.backend.entity.OrderStatus;
import com.foodsystem.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderDto orderDto, 
                                                        Principal principal) {
        OrderResponseDto created = orderService.createOrder(principal.getName(), orderDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id, 
                                                         Principal principal) {
        OrderResponseDto order = orderService.getOrderById(id, principal.getName());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(Principal principal) {
        List<OrderResponseDto> list = orderService.getMyOrders(principal.getName());
        return ResponseEntity.ok(list);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> list = orderService.getAllOrders();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long id, 
                                                              @RequestParam OrderStatus status) {
        OrderResponseDto updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/track")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> trackOrderStatus(@PathVariable Long id, 
                                                   Principal principal) {
        OrderResponseDto order = orderService.getOrderById(id, principal.getName());
        return ResponseEntity.ok("Order Status: " + order.getOrderStatus() + " | Payment Status: " + order.getPaymentStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
