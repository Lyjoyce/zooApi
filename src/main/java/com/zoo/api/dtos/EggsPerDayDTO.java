package com.zoo.api.dtos;

import java.time.LocalDate;

public interface EggsPerDayDTO {
    LocalDate getDate();
    Long getCount();
}

