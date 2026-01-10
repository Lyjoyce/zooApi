package com.zoo.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.api.dtos.AdultTicketRequest;
import com.zoo.api.entities.Ticket;
import com.zoo.api.services.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * Endpoint pour réserver un ticket avec un AdultTicketRequest
     * Retourne ticketNumber et visitDate si succès
     */
    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody AdultTicketRequest request) {
        try {
            Ticket ticket = ticketService.reserveTicket(request);

            // Debug log
            System.out.println("[DEBUG] Ticket réservé : " + ticket.getTicketNumber()
                    + ", pour " + ticket.getAdult().getEmail());

            return ResponseEntity.ok(
                    Map.of(
                        "ticketNumber", ticket.getTicketNumber(),
                        "visitDate", ticket.getVisitDate().toString(),
                        "adultEmail", ticket.getAdult().getEmail()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace(); // Affiche la stacktrace dans la console pour debug
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "error", "Erreur lors de la réservation",
                        "message", e.getMessage()
                    ));
        }
    }

    /**
     * Endpoint pour récupérer tous les tickets
     */
    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketService.getAllTickets();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body(null);
        }
    }
}
