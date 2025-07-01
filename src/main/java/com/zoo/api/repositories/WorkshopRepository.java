package com.zoo.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zoo.api.entities.Workshop;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {}
