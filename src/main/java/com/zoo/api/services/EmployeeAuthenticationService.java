package com.zoo.api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zoo.api.dtos.EmployeeLoginRequest;
import com.zoo.api.dtos.EmployeeLoginResponse;
import com.zoo.api.entities.Account;
import com.zoo.api.enums.Role;
import com.zoo.api.repositories.AccountRepository;
import com.zoo.api.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Méthode de test (optionnelle)
    public void testPassword(String rawPassword, String storedHash) {
        System.out.println("RAW: " + rawPassword);
        System.out.println("HASH: " + storedHash);
        System.out.println("MATCHES: " + passwordEncoder.matches(rawPassword, storedHash));
    }

 // Login
    public EmployeeLoginResponse login(EmployeeLoginRequest request) {
        // ➡️ Ajout du log pour voir si ça passe
        System.out.println("Tentative de login employé avec email = " + request.getEmail());

        // Récupère l'employé par email
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        // Vérifie le mot de passe
        testPassword(request.getPassword(), account.getPassword());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Identifiants invalides");
        }

        // Génère le token JWT en passant Account entier
        String token = jwtUtil.generateTokenForAccount(account);

        // Retourne la réponse avec le token
        return new EmployeeLoginResponse(
                account.getId(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName(),
                account.getRole().name(),
                token
        );
    }

    // Méthode pour créer un nouvel employé avec mot de passe encodé
    public Account registerEmployee(String email, String rawPassword, String firstName, String lastName, String roleStr) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(rawPassword));
        account.setFirstName(firstName);
        account.setLastName(lastName);

        // Parse le rôle de manière sécurisée
        try {
            Role role = Role.valueOf(roleStr.trim().toUpperCase());
            account.setRole(role);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Rôle invalide : " + roleStr);
        }

        return accountRepository.save(account);
    }
}
