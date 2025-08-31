package com.zoo.api.services;

import com.zoo.api.documents.Avis;
import com.zoo.api.repositories.AvisRepository;
import com.zoo.api.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvisService {

    private final AvisRepository avisRepository;
    private final TicketRepository ticketRepository;

    /**
     * Créer un avis avec vérification du ticket associé
     */
    public ResponseEntity<?> createAvisWithTicketVerification(Avis avis) {
        // Vérification des champs obligatoires
        if (avis.getFirstName() == null || avis.getTicketNumber() == null || avis.getVisitDate() == null) {
            return ResponseEntity.badRequest().body(
                Map.of("message", "❌ Informations incomplètes : firstName, ticketNumber et visitDate sont requis.")
            );
        }

        // Vérification que le ticket existe et correspond
        Optional<?> ticketOpt = ticketRepository.findByTicketNumberAndFirstNameAndVisitDate(
            avis.getTicketNumber(),
            avis.getFirstName(),
            avis.getVisitDate()
        );

        if (ticketOpt.isEmpty()) {
            return ResponseEntity.status(401).body(
                Map.of("message", "❌ Ticket non trouvé ou informations incorrectes. Impossible de laisser un avis.")
            );
        }

        avis.setDate(LocalDateTime.now());
        avis.setValidated(false);

        Avis savedAvis = avisRepository.save(avis);

        return ResponseEntity.ok(
            Map.of(
                "message", "✅ Avis créé avec succès (en attente de validation par un employé).",
                "avis", savedAvis
            )
        );
    }

    /**
     * Validation d’un avis par un employé
     */
    public ResponseEntity<?> validerAvis(String id) {
        Optional<Avis> optionalAvis = avisRepository.findById(id);
        if (optionalAvis.isEmpty()) {
            return ResponseEntity.badRequest().body(
                Map.of("message", "❌ Avis introuvable pour l'id : " + id)
            );
        }

        Avis avis = optionalAvis.get();

        if (avis.isValidated()) {
            return ResponseEntity.badRequest().body(
                Map.of("message", "⚠️ L'avis est déjà validé.", "avis", avis)
            );
        }

        avis.setValidated(true);
        Avis updatedAvis = avisRepository.save(avis);

        return ResponseEntity.ok(
            Map.of(
                "message", "✅ Avis validé avec succès par un employé.",
                "avis", updatedAvis
            )
        );
    }
}
