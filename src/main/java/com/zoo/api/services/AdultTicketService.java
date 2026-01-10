package com.zoo.api.services;

import com.zoo.api.dtos.AdultTicketRequest;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Ticket;
import com.zoo.api.enums.AdultType;
import com.zoo.api.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AdultTicketService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    private final AtomicInteger counter = new AtomicInteger(1); // pour l'incrément local

    public String makeReservation(AdultTicketRequest request) {
    	
    	 // Conversion string → enum
        AdultType adultTypeEnum = AdultType.valueOf(request.getAdultType().toUpperCase());

        Adult adult = Adult.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .type(adultTypeEnum)
                .build();

        // 🔹 Génération numéro de réservation
        String dateCode = LocalDate.now().toString().replaceAll("-", "");
        String ticketNumber = "Reservation-" + dateCode + "-" + String.format("%03d", counter.getAndIncrement());

        // 🔹 Création du ticket et lien vers l'adulte
        Ticket ticket = Ticket.builder()
                .ticketNumber(ticketNumber)       // ajouter le numéro
                .visitDate(request.getVisitDate())
                .nbAdults(request.getNbAdults())
                .nbChildren(request.getNbChildren())
                .adult(adult)                     // lien essentiel
                .build();

        // 🔹 Sauvegarde du ticket (cascade persiste l'adulte)
        ticketRepository.save(ticket);

        // 🔹 Envoi email de confirmation
        emailService.sendConfirmationEmail(
                adult.getEmail(),
                adult.getFirstName(),
                ticketNumber,
                request.getVisitDate()
        );

        return ticketNumber;
    }
}
