package com.zoo.api.entities;

import java.util.ArrayList;
import java.util.List;
import com.zoo.api.enums.AdultType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "adult")
@Data
@Builder
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
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();
}