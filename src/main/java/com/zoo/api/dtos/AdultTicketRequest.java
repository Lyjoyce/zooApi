package com.zoo.api.dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdultTicketRequest {

    private Long ticketNumber; // généré automatiquement

    @NotEmpty(message = "Le prénom est obligatoire")
    private String firstName;

    @NotEmpty(message = "Le nom est obligatoire")
    private String lastName;

    @Email(message = "Email invalide")
    @NotEmpty(message = "L'email est obligatoire")
    private String email;

    @NotEmpty(message = "Le type d'adulte est obligatoire")
    private String adultType; // sera converti en AdultType enum

    @NotNull(message = "La date de visite est obligatoire")
    private LocalDate visitDate;

    @Min(value = 0, message = "Le nombre d'enfants doit être >= 0")
    private int nbEnfants;

    @Min(value = 1, message = "Le nombre d'adultes doit être >= 1")
    private int nbAdultes;

    @NotEmpty(message = "Veuillez sélectionner au moins un atelier")
    private List<String> ateliers;
}
