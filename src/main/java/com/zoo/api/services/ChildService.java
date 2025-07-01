package com.zoo.api.services;

import com.zoo.api.entities.Child;
import com.zoo.api.repositories.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;

    public Child saveChild(Child child) {
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
