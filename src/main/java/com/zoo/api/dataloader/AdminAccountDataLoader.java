package com.zoo.api.dataloader;

import com.zoo.api.entities.Account;
import com.zoo.api.enums.Role;
import com.zoo.api.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(0) // Exécute en premier
public class AdminAccountDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminAccountDataLoader.class);

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            // Vérifie si un admin existe déjà
            boolean adminExists = accountRepository.existsByRole(Role.ROLE_ADMIN);
            if (adminExists) {
                logger.info("Admin déjà présent en base, aucun ajout nécessaire.");
                return;
            }

            // Création de l’admin par défaut
            Account admin = Account.builder()
                    .email("admin@zoo.com")
                    .password(passwordEncoder.encode("adminSecure123")) // mot de passe sécurisé
                    .firstName("Super")
                    .lastName("Admin")
                    .role(Role.ROLE_ADMIN)
                    .active(true)
                    .enabled(true)
                    .build();

            accountRepository.save(admin);
            logger.info("Admin créé avec succès : admin@zoo.com");

        } catch (Exception e) {
            logger.error("Erreur lors de la création automatique de l’admin :", e);
        }
    }
}
