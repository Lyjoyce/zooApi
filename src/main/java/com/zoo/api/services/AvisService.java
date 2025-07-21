package com.zoo.api.services;

import com.zoo.api.documents.Avis;
import com.zoo.api.repositories.AvisRepository;
import com.zoo.api.repositories.TicketRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvisService {

    private final AvisRepository avisRepository;
    private final TicketRepository ticketRepository;

    public ResponseEntity<?> createAvisWithTicketVerification(Avis avis) {
        // Vérification des champs obligatoires
        if (avis.getFirstName() == null || avis.getTicketNumber() == null || avis.getVisitDate() == null) {
            return ResponseEntity.badRequest().body("Informations incomplètes : firstName, ticketNumber, visitDate sont requis.");
        }

        // Vérification que le ticket existe et correspond
        Optional<?> ticketOpt = ticketRepository.findByTicketNumberAndFirstNameAndVisitDate(
            avis.getTicketNumber(),
            avis.getFirstName(),
            avis.getVisitDate()
        );

        if (ticketOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Ticket non trouvé ou informations incorrectes. Impossible de laisser un avis.");
        }

        avis.setDate(LocalDateTime.now());
        avis.setValidated(false);

        Avis savedAvis = avisRepository.save(avis);
        return ResponseEntity.ok(savedAvis);
    }

    public Avis validerAvis(String id) {
        Optional<Avis> optionalAvis = avisRepository.findById(id);
        if (optionalAvis.isEmpty()) {
            throw new IllegalArgumentException("Avis introuvable pour l'id : " + id);
        }

        Avis avis = optionalAvis.get();

        if (avis.isValidated()) {
            throw new IllegalStateException("L'avis est déjà validé.");
        }

        avis.setValidated(true);

        return avisRepository.save(avis);
    }
}
