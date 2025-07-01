package com.zoo.api.repositories;

import com.zoo.api.entities.Egg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EggRepository extends JpaRepository<Egg, Long> {
    List<Egg> findByActiveTrue();
    List<Egg> findByUsedFalseAndActiveTrue();
    List<Egg> findByUsedFalseAndActiveTrueOrderByDateLaidAsc();
}
