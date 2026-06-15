package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.dto.CartDto;
import com.foodsystem.backend.dto.CartItemResponseDto;
import com.foodsystem.backend.dto.CartResponseDto;
import com.foodsystem.backend.entity.Cart;
import com.foodsystem.backend.entity.MenuItem;
import com.foodsystem.backend.entity.User;
import com.foodsystem.backend.exception.BadRequestException;
import com.foodsystem.backend.exception.ResourceNotFoundException;
import com.foodsystem.backend.repository.CartRepository;
import com.foodsystem.backend.repository.MenuItemRepository;
import com.foodsystem.backend.repository.UserRepository;
import com.foodsystem.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    @Transactional
    public CartResponseDto addItemToCart(String email, CartDto cartDto) {
        User user = getUser(email);
        MenuItem menuItem = getMenuItem(cartDto.getMenuItemId());

        if (!menuItem.getAvailability()) {
            throw new BadRequestException("Menu item '" + menuItem.getItemName() + "' is currently not available!");
        }

        Optional<Cart> existingCartItem = cartRepository.findByUserAndMenuItem(user, menuItem);

        if (existingCartItem.isPresent()) {
            Cart cart = existingCartItem.get();
            int newQuantity = cart.getQuantity() + cartDto.getQuantity();
            cart.setQuantity(newQuantity);
            cart.setTotalPrice(newQuantity * menuItem.getPrice());
            cartRepository.save(cart);
        } else {
            Cart cart = Cart.builder()
                    .user(user)
                    .menuItem(menuItem)
                    .quantity(cartDto.getQuantity())
                    .totalPrice(cartDto.getQuantity() * menuItem.getPrice())
                    .build();
            cartRepository.save(cart);
        }

        return getCart(email);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getCart(String email) {
        User user = getUser(email);
        List<Cart> cartItems = cartRepository.findByUser(user);

        List<CartItemResponseDto> itemDtos = cartItems.stream()
                .map(this::mapToItemDto)
                .collect(Collectors.toList());

        double grandTotal = itemDtos.stream()
                .mapToDouble(CartItemResponseDto::getTotalPrice)
                .sum();

        return CartResponseDto.builder()
                .items(itemDtos)
                .grandTotal(grandTotal)
                .build();
    }

    @Override
    @Transactional
    public CartResponseDto updateCartItem(String email, CartDto cartDto) {
        User user = getUser(email);
        MenuItem menuItem = getMenuItem(cartDto.getMenuItemId());

        Cart cart = cartRepository.findByUserAndMenuItem(user, menuItem)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in your cart"));

        if (cartDto.getQuantity() <= 0) {
            cartRepository.delete(cart);
        } else {
            cart.setQuantity(cartDto.getQuantity());
            cart.setTotalPrice(cartDto.getQuantity() * menuItem.getPrice());
            cartRepository.save(cart);
        }

        return getCart(email);
    }

    @Override
    @Transactional
    public CartResponseDto removeCartItem(String email, Long menuItemId) {
        User user = getUser(email);
        MenuItem menuItem = getMenuItem(menuItemId);

        Cart cart = cartRepository.findByUserAndMenuItem(user, menuItem)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in your cart"));

        cartRepository.delete(cart);

        return getCart(email);
    }

    @Override
    @Transactional
    public void clearCart(String email) {
        User user = getUser(email);
        cartRepository.deleteByUser(user);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private MenuItem getMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    private CartItemResponseDto mapToItemDto(Cart cart) {
        return CartItemResponseDto.builder()
                .id(cart.getId())
                .menuItemId(cart.getMenuItem().getId())
                .menuItemName(cart.getMenuItem().getItemName())
                .price(cart.getMenuItem().getPrice())
                .quantity(cart.getQuantity())
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
