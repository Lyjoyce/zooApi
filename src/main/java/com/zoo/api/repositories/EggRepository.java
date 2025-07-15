package com.zoo.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zoo.api.dtos.EggsPerDayDTO;
import com.zoo.api.entities.Egg;

@Repository
public interface EggRepository extends JpaRepository<Egg, Long> {

    // Œufs actifs
    List<Egg> findByActiveTrue();

    // Œufs non utilisés et actifs
    List<Egg> findByUsedFalseAndActiveTrue();

    // Œufs non utilisés et actifs, triés par date de ponte croissante (FIFO)
    List<Egg> findByUsedFalseAndActiveTrueOrderByDateLaidAsc();

    // Œufs utilisables : non utilisés, actifs et validés par un vétérinaire
    List<Egg> findByUsedFalseAndActiveTrueAndValidatedByVetTrue();

    // Œufs non utilisés, actifs mais non encore validés
    List<Egg> findByUsedFalseAndActiveTrueAndValidatedByVetFalse();

    // Statistiques : nombre d’œufs pondus par jour
    @Query("SELECT e.dateLaid AS date, COUNT(e) AS count " +
           "FROM Egg e " +
           "GROUP BY e.dateLaid " +
           "ORDER BY e.dateLaid")
    List<EggsPerDayDTO> countEggsPerDayDTO();

}
