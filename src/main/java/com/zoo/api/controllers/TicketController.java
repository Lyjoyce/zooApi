package com.zoo.api.controllers;

import java.util.Map;

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

    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody AdultTicketRequest request) {
        try {
            Ticket ticket = ticketService.reserveTicket(request);

            return ResponseEntity.ok(
                Map.of(
                    "ticketNumber", ticket.getTicketNumber(),
                    "visitDate", ticket.getVisitDate().toString(),
                    "adultEmail", ticket.getAdult().getEmail()
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                Map.of(
                    "error", "Erreur lors de la réservation",
                    "message", e.getMessage()
                )
            );
        }
    }
}