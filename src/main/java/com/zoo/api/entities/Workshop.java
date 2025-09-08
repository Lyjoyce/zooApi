package com.zoo.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.zoo.api.enums.WorkshopType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String startTime;
    private String endTime;

    @Enumerated(EnumType.STRING)
    private WorkshopType type; // OMELETTE, NOURRIR_AUTRUCHES

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "egg_id")
    private Egg allocatedEgg; // uniquement si type == OMELETTE

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    // Méthode pour attribuer un œuf
    public void assignEgg(Egg egg) {
        if (this.type != WorkshopType.OMELETTE) {
            throw new IllegalStateException("Seuls les ateliers OMELETTE peuvent recevoir un œuf.");
        }
        this.allocatedEgg = egg;
        egg.setAllocated(true); // marquer l'œuf comme attribué
    }
}
