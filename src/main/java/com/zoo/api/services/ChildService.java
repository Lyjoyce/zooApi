package com.zoo.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Child;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.repositories.ChildRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
    private final AdultRepository adultRepository;

    public Child saveChild(Child child) {
        if (child.getResponsibleAdult() == null || child.getResponsibleAdult().getId() == null) {
            throw new IllegalArgumentException("L'adulte associé est requis pour enregistrer un enfant.");
        }

        Long adultId = child.getResponsibleAdult().getId();

        Adult adult = adultRepository.findById(adultId)
                .orElseThrow(() -> new IllegalArgumentException("Adulte non trouvé avec l’ID : " + adultId));

        if (adult.getChildren().size() >= 6) {
            throw new IllegalArgumentException("Un adulte ne peut pas avoir plus de 6 enfants.");
        }

        child.setResponsibleAdult(adult); // Associe l’adulte complet à l’enfant
        return childRepository.save(child);
    }

    public Optional<Child> getChildById(Long id) {
        return childRepository.findById(id);
    }

    public List<Child> getAllActiveChildren() {
        return childRepository.findByActiveTrue();
    }

    public Optional<Child> deactivateChild(Long id) {
        return childRepository.findById(id).map(child -> {
            child.setActive(false);
            return childRepository.save(child);
        });
    }

    public void deleteChild(Long id) {
        childRepository.deleteById(id);
    }
}
