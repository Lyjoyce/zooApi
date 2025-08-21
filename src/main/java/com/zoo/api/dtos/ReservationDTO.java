package com.zoo.api.dtos;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReservationDTO {

    private Long id;
    private LocalDate reservationDate;

    private Long createdById; // id de l’adulte qui a créé la réservation

    private int nbChildren; // nombre d’enfants
    private int nbAdults;   // nombre d’adultes

    private List<Long> workshopIds; // ids des ateliers réservés
}



