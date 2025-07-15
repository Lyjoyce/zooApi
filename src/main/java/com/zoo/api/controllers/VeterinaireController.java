package com.zoo.api.controllers;

import com.zoo.api.entities.Egg;
import com.zoo.api.services.EggService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vet")
@RequiredArgsConstructor
public class VeterinaireController {

    private final EggService eggService;

    // Valider un œuf
    @PreAuthorize("hasRole('ROLE_VETERINAIRE')")
    @PutMapping("/validate/{id}")
    public ResponseEntity<Egg> validateEgg(@PathVariable Long id) {
        Egg validated = eggService.validateEgg(id);
        return ResponseEntity.ok(validated);
    }

    // Voir tous les œufs actifs non encore validés
    @PreAuthorize("hasRole('ROLE_VETERINAIRE')")
    @GetMapping("/to-validate")
    public ResponseEntity<?> getUnvalidatedEggs() {
        return ResponseEntity.ok(
            eggService.getAllActiveEggs()
                      .stream()
                      .filter(egg -> !egg.isValidatedByVet())
        );
    }
}

