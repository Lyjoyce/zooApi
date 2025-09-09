package com.zoo.api.mappers;

import com.zoo.api.dtos.ReservationDTO;
import com.zoo.api.dtos.TicketDTO;
import com.zoo.api.entities.Ticket;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TicketMapper {

    private final ReservationMapper reservationMapper;

    public TicketMapper(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    public TicketDTO toDto(Ticket entity) {
        if (entity == null) return null;

        TicketDTO dto = new TicketDTO();
        dto.setId(entity.getId());
        dto.setTicketNumber(entity.getTicketNumber());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setVisitDate(entity.getVisitDate());
        dto.setNbChildren(entity.getNbChildren());
        dto.setNbAdults(entity.getNbAdults());

        // mappe toutes les réservations liées
        if (entity.getReservations() != null) {
            dto.setReservations(
                    entity.getReservations()
                          .stream()
                          .map(reservationMapper::toDto)
                          .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Ticket toEntity(TicketDTO dto) {
        if (dto == null) return null;

        Ticket entity = new Ticket();
        entity.setId(dto.getId());
        entity.setTicketNumber(dto.getTicketNumber());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setVisitDate(dto.getVisitDate());
        entity.setNbChildren(dto.getNbChildren());
        entity.setNbAdults(dto.getNbAdults());

        // reservations à instancier via le service
        return entity;
    }
}

