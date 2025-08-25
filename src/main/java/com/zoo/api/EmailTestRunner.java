package com.zoo.api;

import com.zoo.api.services.EmailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmailTestRunner implements CommandLineRunner {

    private final EmailService emailService;

    public EmailTestRunner(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {
        // Envoie un mail de test à toi-même
        emailService.sendTestEmail("kakti@alwaysdata.net");
        System.out.println("📨 Email de test envoyé à kakti@alwaysdata.net !");
    }
}

