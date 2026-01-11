package com.zoo.api.services;

import com.zoo.api.dtos.AdultTicketRequest;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Ticket;
import com.zoo.api.enums.AdultType;
import com.zoo.api.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AdultTicketService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    private final AtomicInteger counter = new AtomicInteger(1);

    @Transactional
    public String makeReservation(AdultTicketRequest request) {

        // 1️⃣ Conversion enum
        AdultType adultTypeEnum = AdultType.valueOf(request.getAdultType().toUpperCase());

        // 2️⃣ Création Adult (SANS builder)
        Adult adult = new Adult();
        adult.setFirstName(request.getFirstName());
        adult.setLastName(request.getLastName());
        adult.setEmail(request.getEmail());
        adult.setType(adultTypeEnum);
        // tickets déjà initialisé dans l'entité

        // 3️⃣ Génération numéro ticket
        String dateCode = LocalDate.now().toString().replace("-", "");
        String ticketNumber = "Reservation-" + dateCode + "-" +
                String.format("%03d", counter.getAndIncrement());

        // 4️⃣ Création Ticket
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(ticketNumber);
        ticket.setVisitDate(request.getVisitDate());
        ticket.setNbAdults(request.getNbAdults());
        ticket.setNbChildren(request.getNbChildren());

        // 5️⃣ LIAISON BIDIRECTIONNELLE (OBLIGATOIRE)
        ticket.setAdult(adult);
        adult.getTickets().add(ticket);

        // 6️⃣ Sauvegarde (cascade = Adult + Ticket)
        ticketRepository.save(ticket);

        // 7️⃣ Email
        emailService.sendConfirmationEmail(
                adult.getEmail(),
                adult.getFirstName(),
                ticketNumber,
                request.getVisitDate()
        );

        return ticketNumber;
    }
}
