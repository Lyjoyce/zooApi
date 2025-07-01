package com.zoo.api.repositories;

import com.zoo.api.entities.Adult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdultRepository extends JpaRepository<Adult, Long> {
    List<Adult> findByActiveTrue();
}
