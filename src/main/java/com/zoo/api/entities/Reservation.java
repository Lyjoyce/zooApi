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
    
 // Reservation.java
    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToMany
    @JoinTable(
        name = "reservation_workshops",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "workshop_id")
    )

    @ManyToOne
    @JoinColumn(name = "adult_id")
    private Adult createdBy;

    private int nbChildren;
    private int nbAdults;

    @Builder.Default
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workshop> workshops = new ArrayList<>();
}
