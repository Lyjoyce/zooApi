package com.zoo.api.services;

import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AdultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdultService {

    private final AdultRepository adultRepository;

    public List<Adult> getAllActiveAdults() {
        return adultRepository.findByActiveTrue();
    }

    public Optional<Adult> getAdultById(Long id) {
        return adultRepository.findById(id);
    }

    public Adult saveAdult(Adult adult) {
        return adultRepository.save(adult);
    }

    public Optional<Adult> deactivateAdult(Long id) {
        return adultRepository.findById(id).map(adult -> {
            adult.setActive(false);
            return adultRepository.save(adult);
        });
    }

    // Pour une suppression physique, si jamais nécessaire :
    public void deleteAdult(Long id) {
        adultRepository.deleteById(id);
    }
}
