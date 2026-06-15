package com.foodsystem.backend.service;

import com.foodsystem.backend.dto.CartDto;
import com.foodsystem.backend.dto.CartResponseDto;

public interface CartService {
    CartResponseDto addItemToCart(String email, CartDto cartDto);
    CartResponseDto getCart(String email);
    CartResponseDto updateCartItem(String email, CartDto cartDto);
    CartResponseDto removeCartItem(String email, Long menuItemId);
    void clearCart(String email);
}
