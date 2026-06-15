package com.foodsystem.backend.service;

import com.foodsystem.backend.dto.RestaurantDto;

import java.util.List;

public interface RestaurantService {
    RestaurantDto createRestaurant(RestaurantDto restaurantDto);
    RestaurantDto getRestaurantById(Long id);
    List<RestaurantDto> getAllRestaurants();
    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);
    void deleteRestaurant(Long id);
}
