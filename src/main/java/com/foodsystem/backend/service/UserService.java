package com.foodsystem.backend.service;

import com.foodsystem.backend.dto.UserResponseDto;
import com.foodsystem.backend.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserResponseDto getMyProfile(String email);
    UserResponseDto updateMyProfile(String email, UserUpdateDto userUpdateDto);
    List<UserResponseDto> getAllUsers();
}
