package com.zoo.api.services;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Reservation;
import com.zoo.api.entities.Ticket;
import com.zoo.api.entities.Workshop;
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

    /**
     * Création classique avec vérifications (adulte, ratio, jours autorisés, réservation…)
     */
    @Transactional
    public Ticket createTicket(Ticket ticket) {
        if (ticket.getAdult() == null || ticket.getAdult().getId() == null) {
            throw new RuntimeException("L'adulte associé au ticket doit être défini avec un ID.");
        }

        Adult adult = adultRepository.findById(ticket.getAdult().getId())
                .orElseThrow(() -> new RuntimeException("Adult non trouvé en base pour ID : " + ticket.getAdult().getId()));
        ticket.setAdult(adult);

        if (ticket.getNbAdultes() <= 0 || ticket.getNbEnfants() / ticket.getNbAdultes() > 6) {
            throw new IllegalArgumentException("1 adulte pour 6 enfants maximum.");
        }

        if (!JOURS_AUTORISES.contains(ticket.getVisitDate().getDayOfWeek())) {
            throw new IllegalArgumentException("Les ateliers sont uniquement disponibles lundi, mardi, jeudi et vendredi.");
        }

        if (ticket.getTicketNumber() == null || ticket.getTicketNumber().isBlank()) {
            ticket.setTicketNumber("TCK-" + System.currentTimeMillis());
        }

        Ticket savedTicket = ticketRepository.save(ticket);

        List<Workshop> workshops = new ArrayList<>();
        if (ticket.getWorkshops() != null && !ticket.getWorkshops().isEmpty()) {
            for (Workshop w : ticket.getWorkshops()) {
                Workshop workshop = workshopRepository
                        .findByTypeAndDate(w.getType(), ticket.getVisitDate())
                        .orElseThrow(() -> new RuntimeException(
                                "Atelier " + w.getType() + " introuvable pour la date " + ticket.getVisitDate()
                        ));
                workshops.add(workshop);
            }
        }

        Reservation reservation = new Reservation();
        reservation.setCreatedBy(adult);
        reservation.setReservationDate(ticket.getVisitDate());
        reservation.setNbAdults(ticket.getNbAdultes());
        reservation.setNbChildren(ticket.getNbEnfants());
        reservation.setWorkshops(workshops);
        reservation.setTicket(savedTicket);

        reservationRepository.save(reservation);
        savedTicket.getReservations().add(reservation);

        emailService.sendTicketConfirmationEmail(
                savedTicket.getEmail(),
                savedTicket.getFirstName(),
                savedTicket.getLastName(),
                savedTicket.getTicketNumber(),
                savedTicket.getVisitDate()
        );

        return savedTicket;
    }

    @Transactional
    public Ticket createTicketWithWorkshops(Ticket ticket, List<String> ateliers) {
        if (ticket.getAdult() == null || ticket.getAdult().getId() == null) {
            throw new RuntimeException("L'adulte doit être défini avec un ID.");
        }

        Adult adult = adultRepository.findById(ticket.getAdult().getId())
                .orElseThrow(() -> new RuntimeException("Adult non trouvé pour ID : " + ticket.getAdult().getId()));
        ticket.setAdult(adult);

        // Vérifications ratio & jours
        if (ticket.getNbAdultes() <= 0 || ticket.getNbEnfants() / ticket.getNbAdultes() > 6) {
            throw new IllegalArgumentException("1 adulte pour 6 enfants maximum.");
        }
        if (!JOURS_AUTORISES.contains(ticket.getVisitDate().getDayOfWeek())) {
            throw new IllegalArgumentException("Ateliers uniquement lundi, mardi, jeudi et vendredi.");
        }

        if (ticket.getTicketNumber() == null || ticket.getTicketNumber().isBlank()) {
            ticket.setTicketNumber("TCK-" + System.currentTimeMillis());
        }

        // Récupérer les Workshops existants et les associer
        List<Workshop> workshops = ateliers.stream().map(a -> {
            WorkshopType type = WorkshopType.valueOf(a.toUpperCase());
            return workshopRepository.findByTypeAndDate(type, ticket.getVisitDate())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Aucun workshop trouvé pour le type : " + type + " et la date : " + ticket.getVisitDate()
                    ));
        }).toList();

        ticket.setWorkshops(workshops);
        workshops.forEach(w -> w.setTicket(ticket));

        // Créer la réservation associée
        Reservation reservation = new Reservation();
        reservation.setTicket(ticket);
        reservation.setCreatedBy(adult);
        reservation.setReservationDate(ticket.getVisitDate());
        reservation.setNbAdults(ticket.getNbAdultes());
        reservation.setNbChildren(ticket.getNbEnfants());
        reservation.setWorkshops(workshops);

        ticket.setReservation(reservation);

        Ticket saved = ticketRepository.save(ticket);

        // Email
        emailService.sendTicketConfirmationEmail(
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getTicketNumber(),
                saved.getVisitDate()
        );

        return saved;
    }
    
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
