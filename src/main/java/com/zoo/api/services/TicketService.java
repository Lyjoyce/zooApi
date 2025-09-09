package com.zoo.api.services;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.api.dtos.AdultTicketRequest;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Reservation;
import com.zoo.api.entities.Ticket;
import com.zoo.api.entities.Workshop;
import com.zoo.api.enums.AdultType;
import com.zoo.api.enums.WorkshopType;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.repositories.ReservationRepository;
import com.zoo.api.repositories.TicketRepository;
import com.zoo.api.repositories.WorkshopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AdultRepository adultRepository;
    private final WorkshopRepository workshopRepository;
    private final ReservationRepository reservationRepository;
    private final EmailService emailService;

    private static final List<DayOfWeek> JOURS_AUTORISES = List.of(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    );

    @Transactional
    public Ticket createTicket(Ticket ticket, List<String> ateliers) {
        Adult adult = ticket.getAdult();

        if (adult == null || adult.getId() == null) {
            throw new RuntimeException("L'adulte associé au ticket doit être défini avec un ID.");
        }

        // Récupération sécurisée depuis la base
        Adult persistedAdult = adultRepository.findById(adult.getId())
                .orElseThrow(() -> new RuntimeException("Adult non trouvé pour ID : " + adult.getId()));

        if (persistedAdult.getType() == null) {
            throw new IllegalArgumentException("Le type d'adulte doit être défini : PROFESSEUR, PARENT ou AUXILIAIRE.");
        }

        ticket.setAdult(persistedAdult);

        if (ticket.getNbAdults() <= 0 || ticket.getNbChildren() / ticket.getNbAdults() > 6) {
            throw new IllegalArgumentException("1 adulte pour 6 enfants maximum.");
        }

        if (!JOURS_AUTORISES.contains(ticket.getVisitDate().getDayOfWeek())) {
            throw new IllegalArgumentException("Ateliers uniquement lundi, mardi, jeudi et vendredi.");
        }

        if (ticket.getTicketNumber() == null || ticket.getTicketNumber().isBlank()) {
            ticket.setTicketNumber("TCK-" + System.currentTimeMillis());
        }

        return ticketRepository.save(ticket);
    }

    /**
     * Création simplifiée pour la réservation avec AdultTicketRequest
     */
    @Transactional
    public Ticket reserveTicket(AdultTicketRequest request) {
        AdultType type;
        try {
            type = AdultType.valueOf(request.getAdultType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Type d'adulte invalide : " + request.getAdultType());
        }

        Adult adult = Adult.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .type(type)
                .build();
        adultRepository.save(adult);

        Ticket ticket = Ticket.builder()
                .visitDate(request.getVisitDate())
                .nbAdults(request.getNbAdultes())
                .nbChildren(request.getNbEnfants())
                .adult(adult)
                .build();

        return createTicket(ticket, request.getAteliers());
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
