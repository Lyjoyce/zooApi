package com.zoo.api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    public EmployeeLoginResponse login(EmployeeLoginRequest request) {
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

        Account account = accountRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        return new EmployeeLoginResponse(
            account.getId(),
            account.getEmail(),
            account.getFirstName(),
            account.getLastName(),
            account.getRole().name()
        );
    }
}
