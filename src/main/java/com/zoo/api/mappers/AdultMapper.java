package com.zoo.api.mappers;

import com.zoo.api.dtos.AdultDTO;
import com.zoo.api.entities.Adult;
import org.springframework.stereotype.Component;

@Component
public class AdultMapper {

    public AdultDTO toDto(Adult entity) {
        if (entity == null) {
            return null;
        }
        AdultDTO dto = new AdultDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    public Adult toEntity(AdultDTO dto) {
        if (dto == null) {
            return null;
        }
        Adult entity = new Adult();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        return entity;
    }
}
