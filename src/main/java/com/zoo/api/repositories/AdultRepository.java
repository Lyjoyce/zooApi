package com.zoo.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zoo.api.entities.Adult;

@Repository
public interface AdultRepository extends JpaRepository<Adult, Long> {
    Optional<Adult> findByEmail(String email);
}
