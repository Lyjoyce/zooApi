package com.zoo.api.mappers;

import com.zoo.api.dtos.ReservationDTO;
import com.zoo.api.dtos.WorkshopDTO;
import com.zoo.api.entities.Reservation;
import com.zoo.api.entities.Workshop;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    private final WorkshopMapper workshopMapper;

    public ReservationMapper(WorkshopMapper workshopMapper) {
        this.workshopMapper = workshopMapper;
    }

    public ReservationDTO toDto(Reservation entity) {
        if (entity == null) return null;

        ReservationDTO dto = new ReservationDTO();
        dto.setId(entity.getId());
        dto.setReservationDate(entity.getReservationDate());
        dto.setNbChildren(entity.getNbChildren());
        dto.setNbAdults(entity.getNbAdults());

        // mappe les workshops complets
        if (entity.getWorkshops() != null) {
            dto.setWorkshops(
                    entity.getWorkshops()
                          .stream()
                          .map(workshopMapper::toDto)
                          .collect(Collectors.toList())
            );
        }

        // ticket info (createdBy) géré via TicketDTO
        if (entity.getCreatedBy() != null) {
            dto.setCreatedById(entity.getCreatedBy().getId());
        }

        return dto;
    }

    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) return null;

        Reservation entity = new Reservation();
        entity.setId(dto.getId());
        entity.setReservationDate(dto.getReservationDate());
        entity.setNbChildren(dto.getNbChildren());
        entity.setNbAdults(dto.getNbAdults());

        // workshops à récupérer via le service
        return entity;
    }
}
