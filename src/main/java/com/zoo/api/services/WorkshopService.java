package com.zoo.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zoo.api.entities.Egg;
import com.zoo.api.entities.Workshop;
import com.zoo.api.enums.WorkshopType;
import com.zoo.api.repositories.EggRepository;
import com.zoo.api.repositories.WorkshopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final EggRepository eggRepository;
    private final EggService eggService;


    // Attribuer le premier œuf disponible à un atelier omelette
    public Workshop assignEggToWorkshop(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new RuntimeException("Workshop non trouvé"));

        if (workshop.getType() != WorkshopType.OMELETTE) {
            throw new RuntimeException("Seuls les ateliers OMELETTE peuvent recevoir un œuf.");
        }

        // Récupérer le premier œuf disponible et validé
        Egg egg = eggRepository.findByAllocatedFalseAndActiveTrueAndValidatedByVetTrue()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun œuf disponible"));

        // Attribution
        workshop.assignEgg(egg);

        // Sauvegarde
        eggRepository.save(egg); // mise à jour du statut
        return workshopRepository.save(workshop);
    }


    public Workshop saveWorkshop(Workshop workshop) {
        return workshopRepository.save(workshop);
    }

    public Optional<Workshop> getWorkshopById(Long id) {
        return workshopRepository.findById(id);
    }

    public List<Workshop> getAllWorkshops() {
        return workshopRepository.findAll();
    }

    public void deleteWorkshop(Long id) {
        workshopRepository.deleteById(id);
    }
    
    public List<String> getAvailableWorkshops() {
        boolean oeufDispo = eggService.hasAvailableEgg();
        if (oeufDispo) {
            return List.of("OMELETTE", "NOURRIR");
        } else {
            return List.of("NOURRIR");
        }
    }
}

