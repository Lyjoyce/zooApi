package com.zoo.api.services;

import com.zoo.api.entities.Ostrich;
import com.zoo.api.repositories.OstrichRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OstrichService {

    private final OstrichRepository ostrichRepository;

    public Ostrich saveOstrich(Ostrich ostrich) {
        return ostrichRepository.save(ostrich);
    }
    
    public boolean existsByName(String name) {
        return ostrichRepository.existsByName(name);
    }

    public Optional<Ostrich> getOstrichById(Long id) {
        return ostrichRepository.findById(id);
    }

    /**
     * Retourne uniquement une autruche active (active = true)
     */
    public Optional<Ostrich> getActiveOstrichById(Long id) {
        return ostrichRepository.findByIdAndActiveTrue(id);
    }

    public List<Ostrich> getAllActiveOstriches() {
        return ostrichRepository.findByActiveTrue();
    }

    /**
     * Désactivation logique d’une autruche
     */
    public Optional<Ostrich> deactivateOstrich(Long id) {
        return ostrichRepository.findById(id).map(ostrich -> {
            ostrich.setActive(false);
            return ostrichRepository.save(ostrich);
        });
    }

    /**
     * Mise à jour d’une autruche (évite les valeurs null)
     */
    public Optional<Ostrich> updateOstrich(Long id, Ostrich updated) {
        return ostrichRepository.findById(id).map(ostrich -> {
            if (updated.getName() != null) {
                ostrich.setName(updated.getName());
            }
            if (updated.getAge() != null) {
                ostrich.setAge(updated.getAge());
            }
            if (updated.getGender() != null) {
                ostrich.setGender(updated.getGender());
            }
            return ostrichRepository.save(ostrich);
        });
    }
}
