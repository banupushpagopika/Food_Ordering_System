package com.foodsystem.backend.service;

import com.foodsystem.backend.dto.AuthResponse;
import com.foodsystem.backend.dto.LoginRequest;
import com.foodsystem.backend.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
