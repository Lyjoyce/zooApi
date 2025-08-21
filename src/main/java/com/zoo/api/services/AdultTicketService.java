package com.zoo.api.services;

import com.zoo.api.dtos.AdultTicketRequest;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Ticket;
import com.zoo.api.repositories.AdultRepository;
import com.zoo.api.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AdultTicketService {

    private final AdultRepository adultRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    private final AtomicInteger counter = new AtomicInteger(1); // pour l'incrément local (peut être stocké en BDD à terme)

    public String makeReservation(AdultTicketRequest request) {
        Adult adult = new Adult();
        adult.setFirstName(request.getFirstName());
        adult.setLastName(request.getLastName());
        adult.setEmail(request.getEmail());
        //adult.setType(request.getType());

        adult = adultRepository.save(adult);

        String dateCode = LocalDate.now().toString().replaceAll("-", "");
        String ticketNumber = "Reservation-" + dateCode + "-" + String.format("%03d", counter.getAndIncrement());

        Ticket ticket = new Ticket();
        ticket.setTicketNumber(ticketNumber);
        //ticket.setVisitDate(request.getVisitDate());
        ticketRepository.save(ticket);

        // Envoi email
        emailService.sendConfirmationEmail(
                adult.getEmail(),
                adult.getFirstName(),
                ticketNumber,
                request.getVisitDate()
        );

        return ticketNumber;
    }
}
