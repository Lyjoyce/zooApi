package com.zoo.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.api.dtos.AdultTicketRequest;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Ticket;
import com.zoo.api.enums.AdultType;
import com.zoo.api.services.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * Création simple d'un ticket (ancienne route)
     */
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket saved = ticketService.createTicket(ticket);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Réservation complète avec Adult et Workshops
     */
    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody AdultTicketRequest request) {
        try {
            // Conversion sécurisée du type d'adulte
            AdultType type;
            try {
                type = AdultType.valueOf(request.getAdultType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Type d'adulte invalide : " + request.getAdultType());
            }

            // Création de l'adulte
            Adult adult = Adult.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .type(type)
                    .build();

            // Création du ticket
            Ticket ticket = Ticket.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .adultType(request.getAdultType()) // si tu veux garder l'Enum pour info
                    .visitDate(request.getVisitDate())
                    .nbAdultes(request.getNbAdultes())
                    .nbEnfants(request.getNbEnfants())
                    .adult(adult) // juste l’ID de l’adulte
                    .build();

            // Sauvegarde avec workshops
            Ticket saved = ticketService.createTicketWithWorkshops(ticket, request.getAteliers());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Récupérer tous les tickets
     */
    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
}
