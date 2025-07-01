package com.zoo.api.dtos;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private LocalDate reservationDate;
    private Long createdById;         // ID de l’adulte qui crée la réservation
    private List<Long> participantIds; // IDs des enfants participants
}


