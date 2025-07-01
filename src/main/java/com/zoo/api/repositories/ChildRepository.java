package com.zoo.api.repositories;

import com.zoo.api.entities.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByActiveTrue();
}
