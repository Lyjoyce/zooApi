// DataLoader pour les autruches
package com.zoo.api.dataloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoo.api.entities.Ostrich;
import com.zoo.api.services.OstrichService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2) // s'assure que ce loader s'exécute en premier
public class OstrichDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(OstrichDataLoader.class);
    private final OstrichService ostrichService;

    @Override
    public void run(String... args) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = new ClassPathResource("data/autruches.json").getInputStream()) {
            // 🔹 Lecture directe du JSON -> List<Ostrich>
            List<Ostrich> ostriches = mapper.readValue(inputStream, new TypeReference<List<Ostrich>>() {});
            int addedCount = 0;

            for (Ostrich ostrich : ostriches) {
                if (ostrich.getName() == null || ostrich.getName().isBlank()) {
                    logger.warn("⚠️ Autruche ignorée : nom manquant.");
                    continue;
                }
                if (ostrich.getAge() == null || ostrich.getAge() <= 0) {
                    logger.warn("⚠️ Autruche '{}' ignorée : âge invalide '{}'", ostrich.getName(), ostrich.getAge());
                    continue;
                }

                // Vérification existence
                if (!ostrichService.existsByName(ostrich.getName())) {
                    ostrich.setActive(true); // ⚡ Sécurisation : toujours activer à l'insertion
                    ostrichService.saveOstrich(ostrich);
                    addedCount++;
                }
            }

            logger.info("✅ {} autruches ajoutées à la base.", addedCount);

        } catch (Exception e) {
            logger.error("❌ Erreur lors du chargement des autruches depuis le JSON : ", e);
        }
    }
}
