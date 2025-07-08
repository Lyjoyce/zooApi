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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ← Corrigé
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @JsonIgnore // ← Sécurisation de l’API
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // pour Spring Security, def les droits

    @Enumerated(EnumType.STRING)
    private AdultType type; // pour savoir si c'est un professeur, parent, ou auxiliaire.


    private boolean active = true; // ← Pour soft delete

    @OneToMany(mappedBy = "responsibleAdult", cascade = CascadeType.ALL)
    private List<Child> children;
}
