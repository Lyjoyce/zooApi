package com.zoo.api.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AdultReservationRequest {
    private String firstName;
    private String lastName;
    private String email;
    
    //private AdultType type;
    private LocalDate visitDate;
    //private String dayOfWeek;
    //private String ateliers;
    
    public AdultReservationRequest() {}

}


