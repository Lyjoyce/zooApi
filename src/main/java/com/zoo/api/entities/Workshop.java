package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.zoo.api.enums.WorkshopType;

@Entity
@Data
@NoArgsConstructor
public class Workshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private WorkshopType type; // OMELETTE, NOURRIR_AUTRUCHES

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Reservation reservation;

    @OneToOne
    private Egg usedEgg; // uniquement si type == OMELETTE
}
