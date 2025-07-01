package com.zoo.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String schoolClass;  // Exemple : "CE2"
}
