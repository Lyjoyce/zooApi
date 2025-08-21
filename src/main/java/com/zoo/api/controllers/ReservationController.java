package com.zoo.api.controllers;

import com.zoo.api.dtos.ReservationDTO;
import com.zoo.api.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    // Injection par constructeur (bonne pratique)
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO dto) {
        try {
            ReservationDTO saved = reservationService.saveReservation(dto);
            URI location = URI.create("/api/v1/reservations/" + saved.getId());
            return ResponseEntity.created(location).body(saved);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        try {
            ReservationDTO reservation = reservationService.getReservationById(id);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO dto) {
        try {
            ReservationDTO updated = reservationService.updateReservation(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
