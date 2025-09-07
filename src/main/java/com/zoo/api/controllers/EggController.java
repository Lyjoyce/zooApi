package com.zoo.api.controllers;

import com.zoo.api.entities.Egg;
import com.zoo.api.services.EggService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/eggs")
@RequiredArgsConstructor
public class EggController {

    private final EggService eggService;
    
 // Liste des œufs à valider (non utilisés, actifs, pas encore validés, < 10 jours)
    @GetMapping("/to-validate")
    public List<Egg> getEggsToValidate() {
        return eggService.getEggsToValidate();
    }

    // Validation d’un œuf par ID
    @PutMapping("/validate/{id}")
    public ResponseEntity<Egg> validateEgg(@PathVariable Long id) {
        return ResponseEntity.ok(eggService.validateEggById(id));
    }

    @GetMapping
    public ResponseEntity<List<Egg>> getAllEggs() {
        return ResponseEntity.ok(eggService.getAllActiveEggs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Egg> getEggById(@PathVariable Long id) {
        return eggService.getEggById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Egg> createEgg(@RequestBody Egg egg) {
        return ResponseEntity.ok(eggService.saveEgg(egg));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Egg> updateEgg(@PathVariable Long id, @RequestBody Egg updatedEgg) {
        try {
            return ResponseEntity.ok(eggService.updateEgg(id, updatedEgg));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Egg> deactivateEgg(@PathVariable Long id) {
        return eggService.deactivateEgg(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEgg(@PathVariable Long id) {
        eggService.deleteEgg(id);
        return ResponseEntity.noContent().build();
    }

        @PostMapping("/{id}/conserve")
        public Egg conserveEgg(@PathVariable Long id, @RequestParam int days) {
            return eggService.conserveEgg(id, days);
        }
    }

