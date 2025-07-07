package com.zoo.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoo.api.entities.Adult;

public interface AdultRepository extends JpaRepository<Adult, Long> {
    List<Adult> findByActiveTrue();
    Optional<Adult> findByEmail(String email);
}
