package com.zoo.api.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zoo.api.entities.Account;
import com.zoo.api.enums.Role;
import com.zoo.api.repositories.AccountRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AccountRepository accountRepo, PasswordEncoder passwordEncoder) {
        return args -> {

            /*
            // Admin
            if (accountRepo.findByEmail("admin@zoo.com").isEmpty()) {
                Account admin = Account.builder()
                    .email("admin@zoo.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .enabled(true)
                    .firstName("Admin")
                    .lastName("Zoo")
                    .build();

                accountRepo.save(admin);
                System.out.println("Compte admin créé : admin@zoo.com");
            } else {
                System.out.println("Compte admin déjà existant.");
            }

            // Employé
            if (accountRepo.findByEmail("test@example.com").isEmpty()) {
                Account employee = Account.builder()
                    .email("test@example.com")
                    .password(passwordEncoder.encode("secret"))
                    .role(Role.ROLE_EMPLOYEE)
                    .enabled(true)
                    .firstName("Alice")
                    .lastName("Dupont")
                    .build();

                accountRepo.save(employee);
                System.out.println("Compte employé créé : test@example.com");
            } else {
                System.out.println("Compte employé déjà existant.");
            }
            */

        };
    }
}
