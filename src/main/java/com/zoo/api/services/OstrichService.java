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

    public Optional<Ostrich> getOstrichById(Long id) {
        return ostrichRepository.findById(id);
    }

    public List<Ostrich> getAllActiveOstriches() {
        return ostrichRepository.findByActiveTrue();
    }

    public Optional<Ostrich> deactivateOstrich(Long id) {
        return ostrichRepository.findById(id).map(ostrich -> {
            ostrich.setActive(false);
            return ostrichRepository.save(ostrich);
        });
    }

    public void deleteOstrich(Long id) {
        ostrichRepository.deleteById(id);
    }
}
