package com.zoo.api.services;

import com.zoo.api.entities.Egg;
import com.zoo.api.repositories.EggRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
