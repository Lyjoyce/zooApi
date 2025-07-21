package com.zoo.api.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.enabled:false}")
    private boolean mailEnabled;

    public void sendConfirmationEmail(String to, String firstName, String ticketNumber, LocalDate date) {
        if (!mailEnabled) {
            System.out.println("Mail sending is disabled. Skipping email.");
            return;
        }

        String subject = "Votre réservation au Zoo Autruche & Compagnie";
        String body = String.format("""
            Bonjour %s,

            Merci pour votre réservation. Voici votre numéro de ticket :

            Numéro : %s
            Date de visite : %s

            Veuillez conserver ce numéro pour laisser un avis après votre visite.

            À bientôt !

            Zoo Autruche & Compagnie
        """, firstName, ticketNumber, date);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
