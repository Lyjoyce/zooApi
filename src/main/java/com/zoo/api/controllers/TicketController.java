package com.zoo.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zoo.api.dtos.AdultTicketRequest;
import com.zoo.api.entities.Ticket;
import com.zoo.api.services.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // Création simple d'un ticket (ancienne route)
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket saved = ticketService.createTicket(ticket);
            return ResponseEntity.ok(saved);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // 409 = Conflict
        }
    }

    // Route /reserve pour réservation avec association à un Adult
    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody AdultTicketRequest request) {
        try {
            Ticket ticket = ticketService.createTicket(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getAdultType(),
                request.getVisitDate(),
                request.getNbAdultes(),
                request.getNbEnfants(),
                request.getAteliers()
            );

            // La liaison à l'adulte est déjà faite dans TicketService
            return ResponseEntity.ok(ticket);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // Adult non trouvé
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur inattendue : " + e.getMessage());
        }
    }

    // Récupérer tous les tickets
    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
}
