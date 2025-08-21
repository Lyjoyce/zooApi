package com.zoo.api.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reservationDate;

    @ManyToOne
    private Adult createdBy;

 // Nombre d'enfants (au lieu d'une liste de Child)
    private int nbChildren;

    // Nombre d’adultes accompagnateurs (y compris celui qui réserve)
    private int nbAdults;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Workshop> workshops;

}
