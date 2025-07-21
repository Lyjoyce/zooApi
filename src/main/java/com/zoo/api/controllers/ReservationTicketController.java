package com.zoo.api.controllers;

import com.zoo.api.dtos.AdultReservationRequest;
import com.zoo.api.entities.Ticket;
import com.zoo.api.services.TicketService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/reservationTicket")
@RequiredArgsConstructor
public class ReservationTicketController {

    private final TicketService ticketService;

    @PostMapping("/reservationTicket")
    public ResponseEntity<String> reserve(@RequestBody AdultReservationRequest request) {
        Ticket ticket = ticketService.createTicket(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getVisitDate()
        );

        return ResponseEntity.ok("Réservation confirmée – Numéro : " + ticket.getTicketNumber());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace(); // Affiche dans la console
        return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
    }

}
