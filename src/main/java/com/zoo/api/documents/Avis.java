package com.zoo.api.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "avis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avis {

    @Id
    private String id;

    private String ticketNumber;       // Référence au ticket

    private LocalDate visitDate;       // Date de la visite (associée au ticket)

    private String firstName;          // Prénom utilisé pour confirmation (pas de nom complet)

    private String message;            // Contenu de l’avis

    private int note;                  // Note sur 5

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now(); // Date d’envoi

    @Builder.Default
    private boolean validated = false; // Avis non validé par défaut (modération)
}
