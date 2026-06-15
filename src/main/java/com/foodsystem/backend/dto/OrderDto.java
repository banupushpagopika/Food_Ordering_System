package com.foodsystem.backend.dto;

import com.foodsystem.backend.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
