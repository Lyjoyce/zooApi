package com.zoo.api.repositories;

import com.zoo.api.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketNumberAndAdultEmailAndVisitDate(
            String ticketNumber,
            String email,
            LocalDate visitDate
    );

    Optional<Ticket> findByAdultEmailAndVisitDate(String email, LocalDate visitDate);

    boolean existsByTicketNumber(String ticketNumber);
}
