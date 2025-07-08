package com.zoo.api.controllers;

import com.zoo.api.dtos.LoginRequest;
import com.zoo.api.dtos.RegisterRequest;
import com.zoo.api.dtos.AuthenticationResponse;
import com.zoo.api.services.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody@Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}

