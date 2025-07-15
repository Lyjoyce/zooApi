package com.zoo.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.api.dtos.AdultLoginRequest;
import com.zoo.api.dtos.AdultRegisterRequest;
import com.zoo.api.dtos.AuthenticationResponse;
import com.zoo.api.services.AdultAuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AdultAuthenticationController {

    private final AdultAuthenticationService authService;

    // Endpoint de connexion
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AdultLoginRequest request) {
        AuthenticationResponse response = authService.adultLoginRequest(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint d'inscription
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AdultRegisterRequest request) {
        try {
            if (request.getType() == null) {
                return ResponseEntity.badRequest().body("Un adulte doit avoir un type (PARENT, PROFESSEUR, AUXILIAIRE).");
            }

            AuthenticationResponse response = authService.adultRegisterRequest(request);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur");
        }
    }
}
