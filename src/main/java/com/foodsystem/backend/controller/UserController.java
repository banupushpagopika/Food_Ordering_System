package com.foodsystem.backend.controller;

import com.foodsystem.backend.dto.UserResponseDto;
import com.foodsystem.backend.dto.UserUpdateDto;
import com.foodsystem.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<UserResponseDto> getMyProfile(Principal principal) {
        UserResponseDto profile = userService.getMyProfile(principal.getName());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<UserResponseDto> updateMyProfile(@Valid @RequestBody UserUpdateDto updateDto, 
                                                           Principal principal) {
        UserResponseDto updated = userService.updateMyProfile(principal.getName(), updateDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> list = userService.getAllUsers();
        return ResponseEntity.ok(list);
    }
}
