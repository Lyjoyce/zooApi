package com.zoo.api.mappers;

import com.zoo.api.dtos.AdultDTO;
import com.zoo.api.entities.Adult;
import org.springframework.stereotype.Component;

@Component
public class AdultMapper {

    public AdultDTO toDto(Adult adult) {
        if (adult == null) return null;

        AdultDTO dto = new AdultDTO();
        dto.setId(adult.getId());
        dto.setFirstName(adult.getFirstName());
        dto.setLastName(adult.getLastName());
        dto.setEmail(adult.getEmail());
        dto.setType(adult.getType());
        return dto;
    }

    public Adult toEntity(AdultDTO dto) {
        if (dto == null) return null;

        Adult adult = new Adult();
        adult.setId(dto.getId());
        adult.setFirstName(dto.getFirstName());
        adult.setLastName(dto.getLastName());
        adult.setEmail(dto.getEmail());
        adult.setType(dto.getType());
        return adult;
    }
}
