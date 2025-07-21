package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    private boolean confirmed = false;
    
    @ManyToOne
    private Adult adult;
}

