package com.zoo.api.services;

import com.zoo.api.entities.Workshop;
import com.zoo.api.repositories.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final EggService eggService;

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

