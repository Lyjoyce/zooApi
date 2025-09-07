package com.zoo.api.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Ticket;
import com.zoo.api.entities.TicketWorkshop;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.repositories.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AdultRepository adultRepository;
    private final EmailService emailService; // Injection du service email

    private static final List<DayOfWeek> JOURS_AUTORISES = List.of(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    );
    

    public Ticket createTicket(Ticket ticket) {
        // Vérifier si un ticket existe déjà pour cet email et cette date
        ticketRepository.findByEmailAndVisitDate(ticket.getEmail(), ticket.getVisitDate())
                .ifPresent(existing -> {
                    throw new IllegalStateException("Un ticket existe déjà pour " + ticket.getEmail() +
                            " à la date " + ticket.getVisitDate());
                });

        return ticketRepository.save(ticket);
    }
    public Ticket createTicket(String firstName, String lastName, String email, String adultType,
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

String ticketNumber = "TCK-" + System.currentTimeMillis();

Ticket ticket = Ticket.builder()
.ticketNumber(ticketNumber)
.firstName(firstName)
.lastName(lastName)
.email(email)
.adultType(adultType)
.visitDate(visitDate)
.nbAdultes(nbAdultes)
.nbEnfants(nbEnfants)
.confirmed(false)
.workshops(new ArrayList<>())
.build();

ateliers.forEach(atelier -> {
TicketWorkshop workshop = TicketWorkshop.builder()
 .atelier(atelier)
 .ticket(ticket)
 .build();
ticket.getWorkshops().add(workshop);
});

// ✅ Associer l'adulte existant
Adult adult = adultRepository.findByEmail(email)
.orElseThrow(() -> new RuntimeException("Adult non trouvé"));
ticket.setAdult(adult);

Ticket savedTicket = ticketRepository.save(ticket);

emailService.sendTicketConfirmationEmail(
savedTicket.getEmail(),
savedTicket.getFirstName(),
savedTicket.getLastName(),
savedTicket.getTicketNumber(),
savedTicket.getVisitDate()
);

return savedTicket;

    
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
