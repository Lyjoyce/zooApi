package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.zoo.api.documents.Avis;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketNumber;

    private String firstName;
    private String lastName;
    private String email;

    private LocalDate visitDate;
    private int nbEnfants;
    private int nbAdultes;

    @ElementCollection
    private List<String> ateliers;

    @Builder.Default
    private boolean confirmed = false;
}

