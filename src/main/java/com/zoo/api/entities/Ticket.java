package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "ticket")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate visitDate;
    private int nbAdults;
    private int nbChildren;

    @Column(nullable = false, unique = true)
    private String ticketNumber;

    @ElementCollection
    @CollectionTable(
        name = "ticket_ateliers",
        joinColumns = @JoinColumn(name = "ticket_id")
    )
    @Column(name = "atelier")
    private List<String> ateliers = new ArrayList<>();
    
    @ManyToOne(cascade = CascadeType.ALL) // PAS juste PERSIST → ALL inclut PERSIST + MERGE
    @JoinColumn(name = "adult_id", nullable = false)
    private Adult adult;
}