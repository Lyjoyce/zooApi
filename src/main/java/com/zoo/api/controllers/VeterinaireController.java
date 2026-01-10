package com.zoo.api.controllers;

import com.zoo.api.entities.Egg;
import com.zoo.api.services.EggService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/veto")
@RequiredArgsConstructor
public class VeterinaireController {

    private final EggService eggService;

    // -------------------------
    // Valider un œuf
    // -------------------------
  
    @PutMapping("/validate/{id}")
    public ResponseEntity<Egg> validateEgg(@PathVariable Long id) {
        Egg validated = eggService.validateEggById(id);
        return ResponseEntity.ok(validated);
    }

    // -------------------------
    // Voir tous les œufs actifs non encore validés (moins de 10 jours)
    // -------------------------
   
    @GetMapping("/to-validate")
    public ResponseEntity<List<Egg>> getUnvalidatedEggs() {
        return ResponseEntity.ok(eggService.getEggsToValidate());
    }
    
    @PostMapping("/add")
    public ResponseEntity<Egg> addEgg(@RequestBody Egg egg) {
    	Egg created = eggService.createEggForOstrich(egg.getOstrich().getName(), egg.getDateLaid());
        return ResponseEntity.ok(created);
    }

    // -------------------------
    // Voir tous les œufs validés et disponibles pour les ateliers
    // -------------------------

    @GetMapping("/valid")
    public ResponseEntity<List<Egg>> getValidatedEggsForAtelier() {
        return ResponseEntity.ok(eggService.getValidEggsForAtelier());
    }
}
