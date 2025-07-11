package com.zoo.api.controllers;

import com.zoo.api.dtos.AdultDTO;
import com.zoo.api.dtos.LoginRequest;
import com.zoo.api.dtos.RegisterRequest;
import com.zoo.api.dtos.AuthenticationResponse;
import com.zoo.api.services.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

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
    public ResponseEntity<?> register(@RequestBody @Valid AdultDTO adultDTO) {
        System.out.println("Type d'adulte : " + adultDTO.getType());
        

        if (adultDTO.getType() == null) {
            return ResponseEntity.badRequest().body("Un adulte doit avoir un type.");
        }

        try {
            // Construire un RegisterRequest à partir de AdultDTO
            RegisterRequest request = new RegisterRequest();
            request.setFirstName(adultDTO.getFirstName());
            request.setLastName(adultDTO.getLastName());
            request.setEmail(adultDTO.getEmail());
            request.setPhone(adultDTO.getPhone());
            request.setPassword(adultDTO.getPassword());
            request.setType(adultDTO.getType());

            AuthenticationResponse response = authService.register(request);
            return ResponseEntity.ok(Map.of("message", "Inscription OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur");
        }
    }


    // Si besoin, tu peux créer une méthode privée pour mapper AdultDTO -> Adult (entité)
    // private Adult mapToAdultEntity(AdultDTO dto) { ... }
}
