package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zoo.api.enums.AdultType;

@Entity
@Data
@NoArgsConstructor
public class Adult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ← Corrigé
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @Enumerated(EnumType.STRING)
    private AdultType type; // pour savoir si c'est un professeur, parent, ou auxiliaire.
    
}
