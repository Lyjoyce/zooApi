package com.zoo.api.services;

import com.zoo.api.entities.Ticket;
import com.zoo.api.repositories.TicketRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    /**
     * Crée une réservation / ticket, génère un numéro unique,
     * sauvegarde en base, puis envoie un email de confirmation.
     */
    @Transactional
    public Ticket createTicket(String firstName, String lastName, String email, LocalDate visitDate) {
        // Générer un numéro de ticket unique (ex : UUID ou custom)
        String ticketNumber = "Reservation-" + LocalDate.now().toString().replace("-", "") + "-" + UUID.randomUUID().toString().substring(0, 8);

        Ticket ticket = new Ticket();
        ticket.setFirstName(firstName);
        ticket.setLastName(lastName);
        ticket.setEmail(email);
        ticket.setVisitDate(visitDate);
        ticket.setTicketNumber(ticketNumber);
        ticket.setConfirmed(false);

        // Sauvegarde en base
        Ticket savedTicket = ticketRepository.save(ticket);

        // Envoi email confirmation
        emailService.sendConfirmationEmail(email, firstName, ticketNumber, visitDate);

        return savedTicket;
    }

    /**
     * Recherche un ticket via numéro, prénom et date.
     */
    public Optional<Ticket> findTicket(String ticketNumber, String firstName, LocalDate visitDate) {
        return ticketRepository.findByTicketNumberAndFirstNameAndVisitDate(ticketNumber, firstName, visitDate);
    }

    /**
     * Confirme un ticket en mettant le flag confirmed à true.
     * Retourne le ticket mis à jour ou Optional.empty() si non trouvé.
     */
    @Transactional
    public Optional<Ticket> confirmTicket(String ticketNumber, String firstName, LocalDate visitDate) {
        Optional<Ticket> ticketOpt = findTicket(ticketNumber, firstName, visitDate);
        ticketOpt.ifPresent(ticket -> {
            ticket.setConfirmed(true);
            ticketRepository.save(ticket);
        });
        return ticketOpt;
    }
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

}

