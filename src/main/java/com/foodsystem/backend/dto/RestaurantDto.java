package com.foodsystem.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {

    private Long id;

    @NotBlank(message = "Restaurant name is required")
    private String restaurantName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Contact number must be valid digits between 10 and 15")
    private String contactNumber;

    @NotBlank(message = "Opening time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Opening time must be in HH:mm format")
    private String openingTime;

    @NotBlank(message = "Closing time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Closing time must be in HH:mm format")
    private String closingTime;
}
