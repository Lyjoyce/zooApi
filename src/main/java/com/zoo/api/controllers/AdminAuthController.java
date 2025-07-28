package com.zoo.api.controllers;

import com.zoo.api.dtos.AdminLoginRequest;
import com.zoo.api.dtos.AdminLoginResponse;
import com.zoo.api.services.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {
        AdminLoginResponse response = adminService.loginAdmin(request);
        return ResponseEntity.ok(response);
    }
}

