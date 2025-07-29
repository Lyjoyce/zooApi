package com.zoo.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zoo.api.documents.Contact;
import com.zoo.api.repositories.ContactRepository;
import com.zoo.api.services.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactRepository contactRepository;
    private final EmailService emailService;

    /**
     * Enregistre le message de contact en base et envoie un mail à l'adresse commune
     */
    @PostMapping
    public ResponseEntity<String> sendContactMessage(@RequestBody Contact contact) {
        contactRepository.save(contact);
        emailService.sendContactEmail(contact);
        return ResponseEntity.ok("Message envoyé !");
    }
}
