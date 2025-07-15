package com.zoo.api.entities;

import com.zoo.api.enums.Gender;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ostrich {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int age;  // âge en années

    @Enumerated(EnumType.STRING)
    private Gender gender;  // utilisation de l'enum Gender
    
    @Builder.Default
    private boolean active = true; // soft delete par défaut à true
}
