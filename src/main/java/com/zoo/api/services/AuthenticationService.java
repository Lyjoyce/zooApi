package com.zoo.api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zoo.api.dtos.AuthenticationResponse;
import com.zoo.api.dtos.LoginRequest;
import com.zoo.api.dtos.RegisterRequest;
import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AdultRepository adultRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthenticationResponse register(RegisterRequest request) {
    	// Validation : un adulte ne peut pas être à la fois employé/admin et professeur/parent
    	if (request.getRole() != null && request.getType() != null) {
    	    throw new IllegalArgumentException("Un adulte ne peut pas avoir à la fois un rôle et un type.");
    	}
    	
        Adult adult = new Adult();
        adult.setFirstName(request.getFirstName());
        adult.setLastName(request.getLastName());
        adult.setEmail(request.getEmail());
        adult.setPhone(request.getPhone());
        adult.setPassword(passwordEncoder.encode(request.getPassword()));
        adult.setRole(request.getRole());
        adult.setType(request.getType());

        adultRepository.save(adult);

        String token = jwtUtil.generateToken(adult);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(LoginRequest request) {
         authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Adult user = adultRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        String token = jwtUtil.generateToken(user);
        return new AuthenticationResponse(token);
    }
}
