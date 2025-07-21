package com.zoo.api.repositories;

import com.zoo.api.documents.Avis;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvisRepository extends MongoRepository<Avis, String> {
    boolean existsByTicketNumber(String ticketNumber);
 // ➕ Récupérer tous les avis validés
    List<Avis> findByValidatedTrue();
    List<Avis> findByValidatedFalse();
}


