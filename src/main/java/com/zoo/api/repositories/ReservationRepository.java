package com.zoo.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zoo.api.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
