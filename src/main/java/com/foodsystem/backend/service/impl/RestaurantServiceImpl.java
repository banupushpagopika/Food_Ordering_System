package com.foodsystem.backend.service.impl;

import com.foodsystem.backend.dto.RestaurantDto;
import com.foodsystem.backend.entity.Restaurant;
import com.foodsystem.backend.exception.ResourceNotFoundException;
import com.foodsystem.backend.repository.RestaurantRepository;
import com.foodsystem.backend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantDto createRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = mapToEntity(restaurantDto);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return mapToDto(savedRestaurant);
    }

    @Override
    public RestaurantDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        return mapToDto(restaurant);
    }

    @Override
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        restaurant.setRestaurantName(restaurantDto.getRestaurantName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setContactNumber(restaurantDto.getContactNumber());
        restaurant.setOpeningTime(restaurantDto.getOpeningTime());
        restaurant.setClosingTime(restaurantDto.getClosingTime());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return mapToDto(updatedRestaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        restaurantRepository.delete(restaurant);
    }

    private Restaurant mapToEntity(RestaurantDto dto) {
        return Restaurant.builder()
                .id(dto.getId())
                .restaurantName(dto.getRestaurantName())
                .address(dto.getAddress())
                .contactNumber(dto.getContactNumber())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .build();
    }

    private RestaurantDto mapToDto(Restaurant entity) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .restaurantName(entity.getRestaurantName())
                .address(entity.getAddress())
                .contactNumber(entity.getContactNumber())
                .openingTime(entity.getOpeningTime())
                .closingTime(entity.getClosingTime())
                .build();
    }
}
