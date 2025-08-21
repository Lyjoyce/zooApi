package com.zoo.api.services;

import com.zoo.api.dtos.ReservationDTO;
import java.util.List;

public interface ReservationService {
    ReservationDTO saveReservation(ReservationDTO dto);
    List<ReservationDTO> getAllReservations();
    ReservationDTO getReservationById(Long id);
    ReservationDTO updateReservation(Long id, ReservationDTO dto);
    void cancelReservation(Long id);
}

