package com.zoo.api.mappers;

import com.zoo.api.dtos.ChildDTO;
import com.zoo.api.entities.Child;
import org.springframework.stereotype.Component;

@Component
public class ChildMapper {

    public ChildDTO toDto(Child entity) {
        if (entity == null) {
            return null;
        }
        ChildDTO dto = new ChildDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAge(entity.getAge());
        dto.setSchoolClass(entity.getSchoolClass());
        return dto;
    }

    public Child toEntity(ChildDTO dto) {
        if (dto == null) {
            return null;
        }
        Child entity = new Child();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAge(dto.getAge());
        entity.setSchoolClass(dto.getSchoolClass());
        return entity;
    }
}
