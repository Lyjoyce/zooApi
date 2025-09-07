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

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket saved = ticketService.createTicket(ticket);
            return ResponseEntity.ok(saved);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // 409 = Conflict
        }
    }

    @PostMapping ("/reserve")
    public ResponseEntity<?> reserve(@RequestBody AdultTicketRequest request) {
        try {
            Ticket ticket = ticketService.createTicket(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getAdultType(),
                request.getVisitDate(),
                request.getNbEnfants(),   // directement int
                request.getNbAdultes(), 
                request.getAteliers() // déjà une List<String>
            );

            return ResponseEntity.ok(ticket);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur inattendue : " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
    
}
