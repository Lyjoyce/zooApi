package com.zoo.api.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zoo.api.entities.Ticket;
import com.zoo.api.entities.TicketWorkshop;
import com.zoo.api.repositories.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    private static final List<DayOfWeek> JOURS_AUTORISES = List.of(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    );

    public Ticket createTicket(String firstName, String lastName, String email,
                               LocalDate visitDate, int nbAdultes, int nbEnfants,
                               List<String> ateliers) {

        if (nbAdultes <= 0 || nbEnfants / nbAdultes > 6) {
            throw new IllegalArgumentException("1 adulte pour 6 enfants maximum.");
        }

        if (!JOURS_AUTORISES.contains(visitDate.getDayOfWeek())) {
            throw new IllegalArgumentException("Les ateliers sont uniquement disponibles lundi, mardi, jeudi et vendredi.");
        }

        if (ateliers == null || ateliers.isEmpty()) {
            throw new IllegalArgumentException("Veuillez sélectionner au moins un atelier.");
        }

        Ticket ticket = Ticket.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .visitDate(visitDate)
                .nbAdultes(nbAdultes)
                .nbEnfants(nbEnfants)
                .confirmed(false)
                .build();
        
        // Ajout des workshops liés
        ateliers.forEach(atelier -> {
            TicketWorkshop workshop = TicketWorkshop.builder()
                    .atelier(atelier)
                    .ticket(ticket)
                    .build();
            ticket.getWorkshops().add(workshop);
        });

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
