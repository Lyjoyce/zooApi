package com.zoo.api.controllers;

import com.zoo.api.dtos.ChildDTO;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Child;
import com.zoo.api.services.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    // GET tous les enfants actifs
    @GetMapping
    public ResponseEntity<List<ChildDTO>> getAllChildren() {
        List<ChildDTO> childrenDTO = childService.getAllActiveChildren().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(childrenDTO);
    }

    // GET un enfant par ID
    @GetMapping("/{id}")
    public ResponseEntity<ChildDTO> getChildById(@PathVariable Long id) {
        return childService.getChildById(id)
                .map(child -> ResponseEntity.ok(convertToDTO(child)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer un enfant
    @PostMapping
    public ResponseEntity<ChildDTO> createChild(@RequestBody ChildDTO childDTO) {
        try {
            Child savedChild = childService.saveChild(convertToEntity(childDTO));
            return ResponseEntity.ok(convertToDTO(savedChild));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // ou utiliser un message d'erreur personnalisé
        }
    }

    // PUT mettre à jour un enfant
    @PutMapping("/{id}")
    public ResponseEntity<ChildDTO> updateChild(@PathVariable Long id, @RequestBody ChildDTO childDTO) {
        Optional<Child> existingChildOpt = childService.getChildById(id);
        if (existingChildOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Child existingChild = existingChildOpt.get();
        existingChild.setFirstName(childDTO.getFirstName());
        existingChild.setLastName(childDTO.getLastName());
        existingChild.setAge(childDTO.getAge());
        existingChild.setSchoolClass(childDTO.getSchoolClass());

        Adult adult = new Adult();
        adult.setId(childDTO.getAdultId());
        existingChild.setResponsibleAdult(adult);

        try {
            Child updated = childService.saveChild(existingChild);
            return ResponseEntity.ok(convertToDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // PUT désactiver un enfant (soft delete)
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ChildDTO> deactivateChild(@PathVariable Long id) {
        return childService.deactivateChild(id)
                .map(child -> ResponseEntity.ok(convertToDTO(child)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Méthodes de conversion entre DTO et Entity
    private ChildDTO convertToDTO(Child child) {
        return new ChildDTO(
                child.getId(),
                child.getFirstName(),
                child.getLastName(),
                child.getAge(),
                child.getSchoolClass(),
                child.getResponsibleAdult() != null ? child.getResponsibleAdult().getId() : null
        );
    }

    private Child convertToEntity(ChildDTO dto) {
        Child child = new Child();
        child.setId(dto.getId());
        child.setFirstName(dto.getFirstName());
        child.setLastName(dto.getLastName());
        child.setAge(dto.getAge());
        child.setSchoolClass(dto.getSchoolClass());

        if (dto.getAdultId() != null) {
            Adult adult = new Adult();
            adult.setId(dto.getAdultId());
            child.setResponsibleAdult(adult);
        }

        return child;
    }
}
