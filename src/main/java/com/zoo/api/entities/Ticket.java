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
@Table(
    name = "ticket",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "ticket_number")
    }
)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", nullable = false, unique = true)
    private String ticketNumber;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String adultType;

    private LocalDate visitDate;
    private int nbEnfants;
    private int nbAdultes;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private Reservation reservation;


    @ManyToOne
    @JoinColumn(name = "adult_id", nullable = false)
    private Adult adult;

    @Builder.Default
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workshop> workshops = new ArrayList<>();
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @Builder.Default
    private boolean confirmed = false;

    // Génère un numéro unique
    @PrePersist
    public void generateTicketNumber() {
        if (this.ticketNumber == null || this.ticketNumber.isBlank()) {
            this.ticketNumber = "TICKET-" + System.currentTimeMillis();
        }
    }
}
