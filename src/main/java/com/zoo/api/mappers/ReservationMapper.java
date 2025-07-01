package com.zoo.api.mappers;

import com.zoo.api.dtos.ReservationDTO;
import com.zoo.api.entities.Reservation;
import com.zoo.api.entities.Adult;
import com.zoo.api.entities.Child;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    // Convertit Reservation entity en ReservationDTO
    public ReservationDTO toDto(Reservation entity) {
        if (entity == null) {
            return null;
        }
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationDate(entity.getReservationDate());
        
        // récupère l'id de l'adulte créateur
        Adult createdBy = entity.getCreatedBy();
        dto.setCreatedById(createdBy != null ? createdBy.getId() : null);
        
        // récupère les ids des enfants participants
        List<Child> participants = entity.getParticipants();
        if (participants != null) {
            dto.setParticipantIds(
                participants.stream()
                            .map(Child::getId)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    // Convertit ReservationDTO en Reservation entity
    // Attention : ici on ne crée pas les Adult et Child, on met juste les références par id null
    // Il faudra gérer la récupération depuis la base ailleurs (dans service)
    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) {
            return null;
        }
        Reservation entity = new Reservation();
        entity.setReservationDate(dto.getReservationDate());

        if (dto.getCreatedById() != null) {
            Adult adult = new Adult();
            adult.setId(dto.getCreatedById());
            entity.setCreatedBy(adult);
        }

        if (dto.getParticipantIds() != null) {
            List<Child> children = dto.getParticipantIds().stream().map(id -> {
                Child c = new Child();
                c.setId(id);
                return c;
            }).collect(Collectors.toList());
            entity.setParticipants(children);
        }
        return entity;
    }
}

