package com.zoo.api.dataloader;

import com.zoo.api.entities.Account;
import com.zoo.api.enums.Role;
import com.zoo.api.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1) // doit s'exécuter avant Ostrich et Egg
public class AccountDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AccountDataLoader.class);

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        try {
            if (!accountRepository.findAll().isEmpty()) {
                logger.info("Les comptes existent déjà en base, aucun ajout automatique.");
                return;
            }

            List<Account> accounts = List.of(
                Account.builder()
                    .email("employee1@zoo.com")
                    .password(passwordEncoder.encode("employee1@duZOO"))
                    .firstName("Alice")
                    .lastName("Martin")
                    .role(Role.ROLE_EMPLOYEE)
                    .active(true)
                    .enabled(true)
                    .build(),
                Account.builder()
                    .email("employee2@zoo.com")
                    .password(passwordEncoder.encode("employee2@duZOO"))
                    .firstName("Bob")
                    .lastName("Durand")
                    .role(Role.ROLE_EMPLOYEE)
                    .active(true)
                    .enabled(true)
                    .build(),
                Account.builder()
                    .email("employee3@zoo.com")
                    .password(passwordEncoder.encode("employee3@duZOO"))
                    .firstName("Chloé")
                    .lastName("Bernard")
                    .role(Role.ROLE_EMPLOYEE)
                    .active(true)
                    .enabled(true)
                    .build(),
                Account.builder()
                    .email("employee4@zoo.com")
                    .password(passwordEncoder.encode("employee4duZOO"))
                    .firstName("David")
                    .lastName("Lefevre")
                    .role(Role.ROLE_EMPLOYEE)
                    .active(true)
                    .enabled(true)
                    .build(),
                Account.builder()
                    .email("vet1@zoo.com")
                    .password(passwordEncoder.encode("veterinaire1duZOO"))
                    .firstName("Emma")
                    .lastName("Girard")
                    .role(Role.ROLE_VETERINAIRE)
                    .active(true)
                    .enabled(true)
                    .build(),
                Account.builder()
                    .email("vet2@zoo.com")
                    .password(passwordEncoder.encode("veterinaire1duZOO"))
                    .firstName("François")
                    .lastName("Moreau")
                    .role(Role.ROLE_VETERINAIRE)
                    .active(true)
                    .enabled(true)
                    .build()
            );

            accountRepository.saveAll(accounts);
            logger.info(" {} comptes ajoutés automatiquement à la base.", accounts.size());

        } catch (Exception e) {
            logger.error(" Erreur lors de l'ajout automatique des comptes :", e);
        }
    }
}

