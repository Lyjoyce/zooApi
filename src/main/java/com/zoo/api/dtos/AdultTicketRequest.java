package com.zoo.api.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class AdultTicketRequest {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate visitDate;
    private int nbEnfants;
    private int nbAdultes;
    private List<String> ateliers; // directement une liste
}
