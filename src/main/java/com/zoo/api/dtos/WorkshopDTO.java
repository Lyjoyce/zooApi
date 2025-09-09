package com.zoo.api.dtos;

import java.time.LocalDate;
import java.util.List;

import com.zoo.api.enums.WorkshopType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkshopDTO {

    private Long id;
    private WorkshopType type;
    private LocalDate date;
}