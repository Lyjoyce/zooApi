package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reservationDate;

    // Chaque réservation appartient à un ticket
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    // L’adulte qui a créé la réservation
    @ManyToOne
    @JoinColumn(name = "adult_id")
    private Adult createdBy;

    private int nbChildren;
    private int nbAdults;

    // Une réservation peut concerner plusieurs ateliers
    @Builder.Default
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workshop> workshops = new ArrayList<>();
}
