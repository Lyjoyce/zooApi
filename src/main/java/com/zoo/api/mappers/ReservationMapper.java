package com.zoo.api.mappers;

import com.zoo.api.dtos.ReservationDTO;
import com.zoo.api.entities.Reservation;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Workshop;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    // Convertit Reservation entity en ReservationDTO
    public ReservationDTO toDto(Reservation entity) {
        if (entity == null) {
            return null;
        }
        ReservationDTO dto = new ReservationDTO();
        dto.setId(entity.getId());
        dto.setReservationDate(entity.getReservationDate());
        dto.setNbChildren(entity.getNbChildren());
        dto.setNbAdults(entity.getNbAdults());

        // récupère l'id de l'adulte créateur
        Adult createdBy = entity.getCreatedBy();
        dto.setCreatedById(createdBy != null ? createdBy.getId() : null);

        // mappe les IDs des workshops
        if (entity.getWorkshops() != null) {
            dto.setWorkshopIds(
                    entity.getWorkshops().stream()
                          .map(Workshop::getId)
                          .collect(Collectors.toList())
            );
        }

        return dto;
    }

    // Convertit ReservationDTO en Reservation entity
    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) {
            return null;
        }
        Reservation entity = new Reservation();
        entity.setId(dto.getId());
        entity.setReservationDate(dto.getReservationDate());
        entity.setNbChildren(dto.getNbChildren());
        entity.setNbAdults(dto.getNbAdults());

        if (dto.getCreatedById() != null) {
            Adult adult = new Adult();
            adult.setId(dto.getCreatedById());
            entity.setCreatedBy(adult);
        }

        // ⚠️ workshops non instanciés ici -> à gérer dans le service
        return entity;
    }
}
