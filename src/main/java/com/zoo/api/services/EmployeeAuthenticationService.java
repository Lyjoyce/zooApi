package com.zoo.api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zoo.api.dtos.EmployeeLoginRequest;
import com.zoo.api.dtos.EmployeeLoginResponse;
import com.zoo.api.entities.Account;
import com.zoo.api.repositories.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder; // ✅ injecte le PasswordEncoder

 // 🟢 Étape 1 : méthode de test
    public void testPassword(String rawPassword, String storedHash) {
        System.out.println("RAW: " + rawPassword);
        System.out.println("HASH: " + storedHash);
        System.out.println("MATCHES: " + passwordEncoder.matches(rawPassword, storedHash));
    }

    // 🟢 Étape 2 : appel dans le login
    public EmployeeLoginResponse login(EmployeeLoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        // appel de la méthode de test
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

        return new EmployeeLoginResponse(
            account.getId(),
            account.getEmail(),
            account.getFirstName(),
            account.getLastName(),
            account.getRole().name()
        );
    }
}