package com.zoo.api.entities;

import com.zoo.api.enums.AdultType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
