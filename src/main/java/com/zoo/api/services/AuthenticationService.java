package com.zoo.api.services;

import com.zoo.api.dtos.*;
import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AdultRepository adultRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Inscription d’un adulte (professeur ou parent).
     * Seul le champ 'type' doit être renseigné, jamais 'role'.
     * Vérifie l’unicité de l’email.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Validation : seul 'type' doit être précisé
        if (request.getType() == null) {
            throw new IllegalArgumentException("Le type doit être précisé (parent ou professeur).");
        }

        // Vérifie que l'email n’est pas déjà utilisé
        if (adultRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        // Création de l’adulte
        Adult adult = new Adult();
        adult.setFirstName(request.getFirstName());
        adult.setLastName(request.getLastName());
        adult.setEmail(request.getEmail());
        adult.setPhone(request.getPhone());
        adult.setPassword(passwordEncoder.encode(request.getPassword()));
        adult.setType(request.getType());

        adultRepository.save(adult);

        // Génère un token JWT après enregistrement
        String token = jwtUtil.generateToken(adult);
        return new AuthenticationResponse(token);
    }

    /**
     * Authentification d’un adulte via email + mot de passe.
     * Retourne un token JWT en cas de succès.
     */
    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        Adult user = adultRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        String token = jwtUtil.generateToken(user);
        return new AuthenticationResponse(token);
    }

    /**
     * Renvoie les infos utilisateur après inscription (sans token).
     * Peut être utilisé dans un autre contrôleur si besoin.
     */
    public AdultDTO registerAndReturnDTO(RegisterRequest request) {
        if (request.getType() == null) {
            throw new IllegalArgumentException("Le type doit être précisé (parent ou professeur).");
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
            saved.getPassword(),
            saved.getType()
        );
    }
}
