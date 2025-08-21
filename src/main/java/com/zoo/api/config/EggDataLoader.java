// DataLoader pour les œufs
package com.zoo.api.config;

import com.zoo.api.entities.Egg;
import com.zoo.api.entities.Ostrich;
import com.zoo.api.services.EggService;
import com.zoo.api.services.OstrichService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2) // s'assure que ce loader s'exécute après les autruches
public class EggDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EggDataLoader.class);

    private final EggService eggService;
    private final OstrichService ostrichService;

    @Override
    public void run(String... args) {
        try {
            // Vérifier si la table Egg est déjà remplie
            if (!eggService.getAllActiveEggs().isEmpty()) {
                logger.info("ℹ️ Les œufs existent déjà en base, aucun nouvel ajout automatique.");
                return; // on sort pour éviter de dupliquer
            }

            // Récupération de toutes les autruches existantes
            List<Ostrich> ostriches = ostrichService.getAllActiveOstriches();
            if (ostriches.isEmpty()) {
                logger.warn("⚠️ Aucune autruche trouvée, impossible d'ajouter des œufs.");
                return;
            }

            int addedCount = 0;
            LocalDate startDate = LocalDate.now().minusDays(30); // date de départ pour les œufs

            for (int i = 0; i < 30; i++) {
                Egg egg = new Egg();
                egg.setActive(true);
                egg.setUsed(false);
                egg.setValidatedByVet(false);
                egg.setDateLaid(startDate.plusDays(i));

                // Attribution cyclique des œufs aux autruches existantes
                Ostrich ostrich = ostriches.get(i % ostriches.size());
                egg.setOstrich(ostrich);

                // Sauvegarde via le service
                eggService.saveEgg(egg);
                addedCount++;
            }

            logger.info("✅ {} œufs ajoutés automatiquement à la base.", addedCount);

        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'ajout automatique des œufs :", e);
        }
    }

    }
