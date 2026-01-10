package com.zoo.api.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id;
    private String ticketNumber;

    private LocalDate visitDate;

    private int nbChildren;
    private int nbAdults;
    
    private AdultDTO adult;
    
    private List<String> ateliers;

    // Liste des réservations liées à ce ticket
    private List<ReservationDTO> reservations = new ArrayList<>();
}

