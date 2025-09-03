package com.zoo.api.controllers;

import com.zoo.api.services.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Contrôleur REST de test pour vérifier l'envoi (simulé) d'emails.
 * Exemple d'appel :
 *  - http://localhost:8080/test-mail?to=destinataire@test.com
 *  - http://localhost:8080/test-confirmation?to=destinataire@test.com
 */
@RestController
public class MailTestController {

    private final EmailService emailService;

    public MailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Test d'un email simple.
     */
    @GetMapping("/test-mail")
    public String testMail(@RequestParam String to) {
        emailService.sendTestEmail(to);
        return " Email de test simulé pour : " + to;
    }

    /**
     * Test d'un email de confirmation.
     */
    @GetMapping("/test-confirmation")
    public String testConfirmation(@RequestParam String to) {
        emailService.sendConfirmationEmail(to, "TestUser", "TEST-123456", LocalDate.now());
        return " Email de confirmation simulé pour : " + to;
    }
}
