package com.zoo.api.repositories;

import com.zoo.api.entities.Ostrich;
import com.zoo.api.enums.Gender;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OstrichRepository extends JpaRepository<Ostrich, Long> {
    List<Ostrich> findByActiveTrue();
    boolean existsByName(String name);
    Optional<Ostrich> findByName(String name);
    List<Ostrich> findByGenderAndActiveTrue(Gender gender);
    Optional<Ostrich> findByIdAndActiveTrue(Long id);
}
