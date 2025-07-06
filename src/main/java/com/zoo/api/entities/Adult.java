package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
public class Adult {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    
    @JsonIgnore // Ne jamais renvoyer le password dans l’API
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // PROFESSEUR, PARENT, AUXILIAIRE

    private boolean active = true; // soft delete

    @OneToMany(mappedBy = "responsibleAdult")
    private List<Child> children;
}
