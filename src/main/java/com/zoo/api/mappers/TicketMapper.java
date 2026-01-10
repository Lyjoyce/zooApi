package com.zoo.api.mappers;

import com.zoo.api.dtos.AdultDTO;
import com.zoo.api.dtos.TicketDTO;
import com.zoo.api.entities.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final AdultMapper adultMapper;

    public TicketDTO toDto(Ticket ticket) {
        if (ticket == null) return null;

        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setVisitDate(ticket.getVisitDate());
        dto.setNbAdults(ticket.getNbAdults());
        dto.setNbChildren(ticket.getNbChildren());

        // Adult → AdultDTO
        if (ticket.getAdult() != null) {
            AdultDTO adultDTO = adultMapper.toDto(ticket.getAdult());
            dto.setAdult(adultDTO);
        }

        // ⚠️ ateliers : seulement si le champ existe dans Ticket
        if (ticket.getAteliers() != null) {
            dto.setAteliers(ticket.getAteliers());
        }

        return dto;
    }

    public Ticket toEntity(TicketDTO dto) {
        if (dto == null) return null;

        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        ticket.setTicketNumber(dto.getTicketNumber());
        ticket.setVisitDate(dto.getVisitDate());
        ticket.setNbAdults(dto.getNbAdults());
        ticket.setNbChildren(dto.getNbChildren());

        if (dto.getAdult() != null) {
            ticket.setAdult(adultMapper.toEntity(dto.getAdult()));
        }

        if (dto.getAteliers() != null) {
            ticket.setAteliers(dto.getAteliers());
        }

        return ticket;
    }
}
