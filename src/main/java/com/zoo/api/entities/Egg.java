package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Egg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateLaid;
    private boolean used; // utilisé ou pas pour atelier

    private boolean active = true; // soft delete
    
    private boolean validatedByVet = false;

    private LocalDate validationDate;
    private LocalDate conservationEndDate;

    @ManyToOne
    @JoinColumn(name = "ostrich_id")
    private Ostrich ostrich;
}
