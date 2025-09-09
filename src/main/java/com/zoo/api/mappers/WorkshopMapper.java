package com.zoo.api.mappers;

import com.zoo.api.dtos.WorkshopDTO;
import com.zoo.api.entities.Workshop;
import org.springframework.stereotype.Component;

@Component
public class WorkshopMapper {

    /**
     * Convertit une entité Workshop en WorkshopDTO pour l'affichage.
     */
    public WorkshopDTO toDto(Workshop entity) {
        if (entity == null) return null;
        return new WorkshopDTO(
                entity.getId(),
                entity.getType(),
                entity.getDate()
        );
    }

    /**
     * Convertit un WorkshopDTO en entité Workshop.
     * Attention : les IDs dans le DTO sont utilisés uniquement pour la mise à jour.
     */
    public Workshop toEntity(WorkshopDTO dto) {
        if (dto == null) return null;
        Workshop entity = new Workshop();
        entity.setId(dto.getId());
        entity.setType(dto.getType());
        entity.setDate(dto.getDate());
        return entity;
    }
}
