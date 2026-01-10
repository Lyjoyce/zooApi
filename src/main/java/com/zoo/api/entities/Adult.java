package com.zoo.api.entities;

import java.util.ArrayList;
import java.util.List;
import com.zoo.api.enums.AdultType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "adult")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private AdultType type;

    @OneToMany(mappedBy = "adult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    // Constructeur pratique sans builder
    public Adult(String firstName, String lastName, String email, AdultType type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.type = type;
        this.tickets = new ArrayList<>();
    }
}