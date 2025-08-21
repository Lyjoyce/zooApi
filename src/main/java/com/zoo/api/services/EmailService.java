package com.zoo.api.services;

import com.zoo.api.documents.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@ConditionalOnProperty(name = "mail.enabled", havingValue = "true", matchIfMissing = true)
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${spring.mail.username:no-reply@zoo-autruche.com}") // l’adresse expéditeur
    private String fromAddress;

    /**
     * Envoi d’un email de confirmation de réservation.
     */
    public void sendConfirmationEmail(String to, String firstName, String ticketNumber, LocalDate date) {
        if (!mailEnabled) {
            System.out.println("Mail sending is disabled. Skipping confirmation email.");
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
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    /**
     * Envoi d’un email de contact (formulaire site web).
     */
    public void sendContactEmail(Contact contact) {
        if (!mailEnabled) {
            System.out.println("Mail sending is disabled. Skipping contact email.");
            return;
        }

        String subject = "[Contact] " + contact.getSubject();
        String body = String.format("""
            Message reçu depuis le formulaire de contact :

            Nom : %s
            Email : %s

            Message :
            %s
        """, contact.getName(), contact.getEmail(), contact.getMessage());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo("autruchecompagnie@gmail.com"); // boîte de réception
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
