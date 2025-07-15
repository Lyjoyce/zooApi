package com.zoo.api.services;

import com.zoo.api.dtos.*;
import com.zoo.api.entities.Adult;
import com.zoo.api.enums.Role;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdultAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AdultRepository adultRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Enregistrement d'un adulte et génération du token JWT.
     */
    public AuthenticationResponse adultRegisterRequest(AdultRegisterRequest request) {
        if (request.getType() == null) {
            throw new IllegalArgumentException("Le type doit être précisé (parent, professeur ou auxiliaire).");
        }

        if (adultRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        Adult adult = new Adult();
        adult.setFirstName(request.getFirstName());
        adult.setLastName(request.getLastName());
        adult.setEmail(request.getEmail());
        adult.setPhone(request.getPhone());
        adult.setPassword(passwordEncoder.encode(request.getPassword()));
        adult.setType(request.getType());

        adultRepository.save(adult);

        String token = jwtUtil.generateToken(adult);
        return new AuthenticationResponse(token);
    }

    /**
     * Authentification d'un adulte avec gestion d’erreur.
     */
    public AuthenticationResponse adultLoginRequest(AdultLoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        Adult adult = adultRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        String token = jwtUtil.generateToken(adult);
        return new AuthenticationResponse(token);
    }

    /**
     * Variante de l'enregistrement qui retourne un DTO sans token.
     */
    public AdultDTO registerAndReturnDTO(AdultRegisterRequest request) {
        if (request.getType() == null) {
            throw new IllegalArgumentException("Le type doit être précisé.");
        }

        if (adultRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        Adult adult = new Adult();
        adult.setFirstName(request.getFirstName());
        adult.setLastName(request.getLastName());
        adult.setEmail(request.getEmail());
        adult.setPhone(request.getPhone());
        adult.setPassword(passwordEncoder.encode(request.getPassword()));
        adult.setType(request.getType());

        Adult saved = adultRepository.save(adult);

        return new AdultDTO(
            saved.getId(),
            saved.getFirstName(),
            saved.getLastName(),
            saved.getEmail(),
            saved.getPhone(),
            null,
            saved.getType(),
            Role.ROLE_ADULTE
        );
    }
}
