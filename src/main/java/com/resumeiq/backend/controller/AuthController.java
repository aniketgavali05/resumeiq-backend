package com.resumeiq.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.backend.request.LoginRequest;
import com.resumeiq.backend.request.RegisterRequest;
import com.resumeiq.backend.response.ApiResponse;
import com.resumeiq.backend.response.AuthenticationResponse;
import com.resumeiq.backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthenticationResponse response = authService.register(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User registered successfully",
                        response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthenticationResponse response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Login successful",
                        response));
    }
}