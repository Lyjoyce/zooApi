package com.zoo.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;

    private boolean active = true;  // actif par défaut

    @OneToMany(mappedBy = "employee")
    private List<Workshop> assignedWorkshops;
}


