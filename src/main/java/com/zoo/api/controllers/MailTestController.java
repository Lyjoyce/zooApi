package com.zoo.api.controllers;

import com.zoo.api.services.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class MailTestController {

    private final EmailService emailService;

    public MailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Exemple : http://localhost:8080/test-mail?to=destinataire@test.com
     */
    @GetMapping("/test-mail")
    public String testMail(@RequestParam String to) {
        try {
            emailService.sendConfirmationEmail(to, "TestUser", "TEST-123456", LocalDate.now());
            return " Email envoyé avec succès à " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Erreur lors de l'envoi de l'email : " + e.getMessage();
        }
    }
}

