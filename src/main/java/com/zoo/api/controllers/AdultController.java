package com.zoo.api.controllers;

import com.zoo.api.documents.Avis;
import com.zoo.api.entities.Adult;
import com.zoo.api.services.AdultService;
import com.zoo.api.services.TicketService;
import com.zoo.api.services.AvisService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/adults")
@RequiredArgsConstructor
public class AdultController {

    private final AdultService adultService;
    private final AvisService avisService;
    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<Adult> getAdultById(@PathVariable Long id) {
        Optional<Adult> adultOpt = adultService.getAdultById(id);
        return adultOpt.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Adult> createAdult(@RequestBody Adult adult) {
        Adult savedAdult = adultService.saveAdult(adult);
        return ResponseEntity.ok(savedAdult);
    }

    /**
     * Création d’un avis via ticket vérifié (nom, numéro, date).
     */
    @PostMapping("/avis")
    public ResponseEntity<?> createAvisViaTicket(@RequestBody Avis avis) {
        return avisService.createAvisWithTicketVerification(avis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adult> updateAdult(@PathVariable Long id, @RequestBody Adult updatedAdult) {
        Optional<Adult> adultOpt = adultService.getAdultById(id);
        if (adultOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Adult adult = adultOpt.get();
        adult.setFirstName(updatedAdult.getFirstName());
        adult.setLastName(updatedAdult.getLastName());
        adult.setEmail(updatedAdult.getEmail());
        adult.setType(updatedAdult.getType());

        Adult savedAdult = adultService.saveAdult(adult);
        return ResponseEntity.ok(savedAdult);
    }
}
