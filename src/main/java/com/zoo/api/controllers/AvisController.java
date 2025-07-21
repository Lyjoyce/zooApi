package com.zoo.api.controllers;

import com.zoo.api.documents.Avis;
import com.zoo.api.repositories.AvisRepository;
import com.zoo.api.services.AvisService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avis")
@RequiredArgsConstructor
public class AvisController {

    private final AvisRepository avisRepository;
    private final AvisService avisService;

    // Récupérer tous les avis non validés (validés == false)
    @GetMapping("/non-valides")
    public ResponseEntity<List<Avis>> getAvisNonValides() {
        List<Avis> avisNonValides = avisRepository.findByValidatedFalse();
        return ResponseEntity.ok(avisNonValides);
    }

    // Lecture publique uniquement des avis validés
    @GetMapping
    public List<Avis> getAvisValides() {
        return avisRepository.findByValidatedTrue();
    }

    // Ajout d’un avis (ticketNumber + firstName + visitDate requis)
    @PostMapping
    public ResponseEntity<?> envoyerAvis(@RequestBody Avis avis) {
        ResponseEntity<?> response = avisService.createAvisWithTicketVerification(avis);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Avis enregistré et en attente de validation.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }
    }
}
