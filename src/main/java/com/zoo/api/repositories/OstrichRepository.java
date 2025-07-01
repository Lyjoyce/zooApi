package com.zoo.api.repositories;

import com.zoo.api.entities.Ostrich;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OstrichRepository extends JpaRepository<Ostrich, Long> {
    List<Ostrich> findByActiveTrue();
}
