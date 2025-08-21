package com.zoo.api.repositories;

import com.zoo.api.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zoo.api.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketNumberAndFirstNameAndVisitDate(
        String ticketNumber,
        String firstNam,
       LocalDate visitDate
    );
}

