package com.zoo.api.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AdultTicketRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String adultType;
    private LocalDate visitDate;
    private int nbAdults;
    private int nbChildren;
    private List<String> ateliers;
}
