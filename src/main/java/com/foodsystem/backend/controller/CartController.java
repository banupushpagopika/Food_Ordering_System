package com.foodsystem.backend.controller;

import com.foodsystem.backend.dto.CartDto;
import com.foodsystem.backend.dto.CartResponseDto;
import com.foodsystem.backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(Principal principal) {
        CartResponseDto cart = cartService.getCart(principal.getName());
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<CartResponseDto> addItemToCart(@Valid @RequestBody CartDto cartDto, 
                                                         Principal principal) {
        CartResponseDto cart = cartService.addItemToCart(principal.getName(), cartDto);
        return ResponseEntity.ok(cart);
    }

    @PutMapping
    public ResponseEntity<CartResponseDto> updateCartItem(@Valid @RequestBody CartDto cartDto, 
                                                          Principal principal) {
        CartResponseDto cart = cartService.updateCartItem(principal.getName(), cartDto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/item/{menuItemId}")
    public ResponseEntity<CartResponseDto> removeCartItem(@PathVariable Long menuItemId, 
                                                            Principal principal) {
        CartResponseDto cart = cartService.removeCartItem(principal.getName(), menuItemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
