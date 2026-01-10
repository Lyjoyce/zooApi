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

    private static final List<DayOfWeek> ALLOWED_DAYS = List.of(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    );

    @Transactional
    public Ticket reserveTicket(AdultTicketRequest request) {

        log.info("Réservation reçue : {}", request);

        AdultType type;
        try {
            type = AdultType.valueOf(request.getAdultType().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Type d'adulte invalide");
        }

        if (!ALLOWED_DAYS.contains(request.getVisitDate().getDayOfWeek())) {
            throw new IllegalArgumentException("Jour non autorisé");
        }

        if (request.getNbAdults() <= 0 ||
            request.getNbChildren() / request.getNbAdults() > 6) {
            throw new IllegalArgumentException("1 adulte pour 6 enfants maximum");
        }

        //  Adult (NOUVEAU)
        Adult adult = Adult.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .type(type)
                .build();

        //  Ticket (CASCADE PERSIST)
        Ticket ticket = Ticket.builder()
                .visitDate(request.getVisitDate())
                .nbAdults(request.getNbAdults())
                .nbChildren(request.getNbChildren())
                .adult(adult)
                .ateliers(request.getAteliers())
                .build();

        //  OBLIGATOIRE : génération du numéro de ticket
        ticket.setTicketNumber("TCK-" + System.currentTimeMillis());
        log.info("Ticket avant sauvegarde : {}", ticket);

        Ticket saved = ticketRepository.save(ticket);

        log.info("Ticket sauvegardé id={} numéro={}",
                saved.getId(), saved.getTicketNumber());

        emailService.sendConfirmationEmail(
                adult.getEmail(),
                adult.getFirstName(),
                saved.getTicketNumber(),
                saved.getVisitDate()
        );

        return saved;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
