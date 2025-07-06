package com.zoo.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AdultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdultService {

    private final AdultRepository adultRepository;
    private final PasswordEncoder passwordEncoder;

    public Adult saveAdult(Adult adult) {
        // Hasher le mot de passe uniquement s'il est en clair
        if (adult.getPassword() != null) {
            adult.setPassword(passwordEncoder.encode(adult.getPassword()));
        }
        return adultRepository.save(adult);
    }

    public List<Adult> getAllActiveAdults() {
        return adultRepository.findByActiveTrue();
    }

    public Optional<Adult> getAdultById(Long id) {
        return adultRepository.findById(id);
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
