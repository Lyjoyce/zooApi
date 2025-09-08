package com.zoo.api.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zoo.api.entities.Workshop;
import com.zoo.api.enums.WorkshopType;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    @Query("SELECT w.date AS date, COUNT(w) AS count FROM Workshop w WHERE w.type = 'OMELETTE' GROUP BY w.date")
    List<Object[]> countOmeletteWorkshopsPerDay();
    Optional<Workshop> findByTypeAndDate(WorkshopType type, LocalDate date);
}