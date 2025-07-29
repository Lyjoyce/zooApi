package com.zoo.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zoo.api.documents.Contact;

public interface ContactRepository extends MongoRepository<Contact, String> {
}

