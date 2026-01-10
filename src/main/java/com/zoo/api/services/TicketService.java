package com.zoo.api.services;

import com.zoo.api.dtos.AdultTicketRequest;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Ticket;
import com.zoo.api.enums.AdultType;
import com.zoo.api.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    // Jours autorisés pour la visite
    private static final List<DayOfWeek> ALLOWED_DAYS = List.of(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    );

    @Transactional
    public Ticket reserveTicket(AdultTicketRequest request) {

        log.info("Réservation reçue : {}", request);

        // 1️ Vérification du type d'adulte
        AdultType type;
        try {
            type = AdultType.valueOf(request.getAdultType().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Type d'adulte invalide : " + request.getAdultType());
        }

        // 2️ Vérification du jour
        if (!ALLOWED_DAYS.contains(request.getVisitDate().getDayOfWeek())) {
            throw new IllegalArgumentException("Jour non autorisé pour la visite : " + request.getVisitDate().getDayOfWeek());
        }

        // 3️ Vérification du ratio adulte/enfants
        if (request.getNbAdults() <= 0 ||
            request.getNbChildren() / request.getNbAdults() > 6) {
            throw new IllegalArgumentException("1 adulte pour 6 enfants maximum");
        }

        // 4️ Création de l'objet Adult
        Adult adult = Adult.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .type(type)
                .build();

        // 5️ Création du Ticket et association à l'Adult
        Ticket ticket = Ticket.builder()
                .visitDate(request.getVisitDate())
                .nbAdults(request.getNbAdults())
                .nbChildren(request.getNbChildren())
                .ticketNumber("TCK-" + System.currentTimeMillis())
                .ateliers(request.getAteliers())
                .adult(adult)
                .build();

        //  Ajout du ticket dans la liste de l'adulte pour Hibernate
        adult.getTickets().add(ticket);

        log.info("Ticket avant sauvegarde : {}", ticket);

        // 6️ Sauvegarde (cascade persiste automatiquement l'adulte)
        Ticket saved = ticketRepository.save(ticket);

        log.info("Ticket sauvegardé id={} numéro={}", saved.getId(), saved.getTicketNumber());

        // 7️ Envoi email de confirmation
        emailService.sendConfirmationEmail(
                adult.getEmail(),
                adult.getFirstName(),
                saved.getTicketNumber(),
                saved.getVisitDate()
        );

        return saved;
    }

    // Récupération de tous les tickets
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}