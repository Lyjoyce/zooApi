package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private int age;
    private String schoolClass; // Exemple : "CE2"

    private boolean active = true; // soft delete

    @ManyToOne
    private Adult adult;
}
