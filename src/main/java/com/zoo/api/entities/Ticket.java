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
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Numéro de ticket généré automatiquement
    private String ticketNumber;

    private String firstName;
    private String lastName;
    private String email;
    @Column(nullable = false)
    private String adultType;

    private LocalDate visitDate;
    private int nbEnfants;
    private int nbAdultes;

    @Builder.Default
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketWorkshop> workshops = new ArrayList<>();

    @Builder.Default
    private boolean confirmed = false;

    // Génère un ticketNumber simple de type "TICKET-xxxx"
    @PrePersist
    public void generateTicketNumber() {
        if (this.ticketNumber == null || this.ticketNumber.isBlank()) {
            this.ticketNumber = "TICKET-" + System.currentTimeMillis();
        }
    }
}
