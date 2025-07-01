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
        return eggService.getEggById(id)
                .map(egg -> {
                    egg.setDateLaid(updatedEgg.getDateLaid());
                    egg.setUsed(updatedEgg.isUsed());
                    egg.setFemale(updatedEgg.getFemale());
                    return ResponseEntity.ok(eggService.saveEgg(egg));
                }).orElse(ResponseEntity.notFound().build());
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
}

