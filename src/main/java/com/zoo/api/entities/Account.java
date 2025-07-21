package com.zoo.api.entities;

import com.zoo.api.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String password;
  
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active = true;
    private boolean enabled = true; // pour suspendre un compte


    // Relation avec un employé (optionnel)
    @OneToOne(mappedBy = "account")
    private Employee employee;
}
