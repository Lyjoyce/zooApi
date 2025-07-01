package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Egg {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate dateLaid;
    private boolean used; // utilisé ou pas pour atelier

    private boolean active = true; // soft delete

    @ManyToOne
    private Ostrich female; // femelle pondeuse
}
