package com.zoo.api.dtos;

import com.zoo.api.enums.AdultType;
import com.zoo.api.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdultDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private AdultType type;
}


   

