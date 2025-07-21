package com.zoo.api.dtos;

import com.zoo.api.enums.AdultType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdultReservationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private AdultType type;
    private LocalDate visitDate;
    private String dayOfWeek;
    private String ateliers;
}


