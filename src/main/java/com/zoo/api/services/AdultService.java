package com.zoo.api.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zoo.api.entities.Adult;
import com.zoo.api.repositories.AdultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdultService {

    private final AdultRepository adultRepository;

    public Optional<Adult> getAdultById(Long id) {
        return adultRepository.findById(id);
    }

    public Adult saveAdult(Adult adult) {
        return adultRepository.save(adult);
    }
}
