package com.zoo.api.repositories;

import com.zoo.api.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketNumberAndFirstNameAndVisitDate(
        String ticketNumber,
        String firstName,
        LocalDate visitDate
    );
}

