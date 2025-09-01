package com.zoo.api.dataloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(0) // doit s'exécuter avant les autres loaders
public class AdminAccountDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminAccountDataLoader.class);

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            if (accountRepository.existsByRole(Role.ROLE_ADMIN)) {
                logger.info("Admin déjà présent en base, aucun ajout nécessaire.");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Account>> typeReference = new TypeReference<>() {};
            InputStream inputStream = TypeReference.class.getResourceAsStream("data/adminAccount.json");

            if (inputStream == null) {
                logger.error("❌ admin_account.json introuvable !");
                return;
            }

            List<Account> admins = mapper.readValue(inputStream, typeReference);

            for (Account admin : admins) {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                accountRepository.save(admin);
            }

            logger.info("Admin ajouté en base depuis adminAccount.json.");

        } catch (Exception e) {
            logger.error("Erreur lors du chargement automatique de l’admin :", e);
        }
    }
}
