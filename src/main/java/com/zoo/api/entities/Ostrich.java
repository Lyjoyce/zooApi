package com.zoo.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zoo.api.enums.Gender;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@JsonIgnoreProperties(ignoreUnknown = true)

public class Ostrich {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    @JsonProperty("nom")
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Positive(message = "Age must be positive")
    private Integer age; // âge en années

    @Enumerated(EnumType.STRING)
    private Gender gender;  // utilisation de l'enum Gender
    
    @Builder.Default
    private boolean active = true; // soft delete par défaut à true
}
