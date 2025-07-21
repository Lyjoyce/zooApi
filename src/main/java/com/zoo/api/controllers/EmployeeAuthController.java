package com.zoo.api.controllers;

import com.zoo.api.dtos.EmployeeLoginRequest;
import com.zoo.api.dtos.EmployeeLoginResponse;
import com.zoo.api.services.EmployeeAuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeAuthController {

    private final EmployeeAuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<EmployeeLoginResponse> login(@RequestBody EmployeeLoginRequest request) {
        EmployeeLoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
