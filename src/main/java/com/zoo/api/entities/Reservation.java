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
    @GeneratedValue
    private Long id;

    private LocalDate reservationDate;

    @ManyToOne
    private Adult createdBy;

    @OneToMany
    private List<Child> participants;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Workshop> workshops;
}
