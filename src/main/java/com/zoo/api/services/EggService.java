package com.zoo.api.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zoo.api.entities.Egg;
import com.zoo.api.repositories.EggRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class EggService {

    private final EggRepository eggRepository;
    
    public List<Egg> getAvailableEggsOrderedByDate(int quantity) {
        List<Egg> availableEggs = eggRepository.findByUsedFalseAndActiveTrueOrderByDateLaidAsc();
        if (availableEggs.size() < quantity) {
            throw new IllegalArgumentException("Pas assez d'œufs disponibles.");
        }
        return availableEggs.subList(0, quantity);
    }
    
    public List<Egg> getValidEggsForAtelier() {
        return eggRepository.findByUsedFalseAndActiveTrueAndValidatedByVetTrue();
    }
    public List<Egg> getEggsToValidate() {
        LocalDate today = LocalDate.now();
        return eggRepository.findByUsedFalseAndActiveTrueAndValidatedByVetFalse()
            .stream()
            .filter(egg -> ChronoUnit.DAYS.between(egg.getDateLaid(), today) <= 10)
            .toList();
    }

    public Egg validateEggById(Long id) {
        Egg egg = eggRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Egg not found"));

        egg.setValidatedByVet(true);
        egg.setValidationDate(LocalDate.now());
        return eggRepository.save(egg);
    }


    
    public Egg validateEgg(Long eggId) {
        Egg egg = eggRepository.findById(eggId)
            .orElseThrow(() -> new IllegalArgumentException("Œuf non trouvé"));

        if (egg.getDateLaid().plusDays(10).isBefore(LocalDate.now())) {
            throw new IllegalStateException("L'œuf a plus de 10 jours, il n'est plus valide.");
        }

        egg.setValidatedByVet(true);
        egg.setValidationDate(LocalDate.now());
        return eggRepository.save(egg);
    }


    
    public boolean hasAvailableEgg() {
        return !eggRepository.findByUsedFalseAndActiveTrue().isEmpty();
    }
    
    public Optional<Egg> getOneAvailableEgg() {
        return eggRepository.findByUsedFalseAndActiveTrue()
                            .stream()
                            .findFirst();
    }

    public Egg saveEgg(Egg egg) {
        return eggRepository.save(egg);
    }

    public Optional<Egg> getEggById(Long id) {
        return eggRepository.findById(id);
    }

    public List<Egg> getAllActiveEggs() {
        return eggRepository.findByActiveTrue();
    }

    public Optional<Egg> deactivateEgg(Long id) {
        return eggRepository.findById(id).map(egg -> {
            egg.setActive(false);
            return eggRepository.save(egg);
        });
    }

    public void deleteEgg(Long id) {
        eggRepository.deleteById(id);
    }
}
