package com.zoo.api.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zoo.api.dtos.AuthenticationResponse;
import com.zoo.api.dtos.LoginRequest;
import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdultRepository adultRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationResponse login(LoginRequest request) {
        Adult adult = adultRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), adult.getPassword())) {
            throw new BadCredentialsException("Mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(adult);
        return new AuthenticationResponse(token);
    }

}

