package com.zoo.api.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.zoo.api.entities.Account;
import com.zoo.api.enums.Role;
import com.zoo.api.repositories.AccountRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(AccountRepository accountRepo) {
        return args -> {
            if (accountRepo.findByEmail("admin@zoo.com").isEmpty()) {
                Account admin = new Account();
                admin.setEmail("admin@zoo.com");
                admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
                admin.setRole(Role.ROLE_ADMIN);
                admin.setEnabled(true);
                accountRepo.save(admin);

                System.out.println("✔ Compte admin créé : admin@zoo.com / admin123");
            } else {
                System.out.println("ℹ Compte admin déjà existant.");
            }
        };
    }
}

