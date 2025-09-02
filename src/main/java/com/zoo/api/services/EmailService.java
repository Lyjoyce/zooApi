package com.zoo.api.services;

import com.zoo.api.documents.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Test simple d'envoi de mail
     */
    public void sendTestEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kakti@alwaysdata.net"); // expéditeur Alwaysdata
        message.setTo(to);
        message.setSubject(" Test email Zoo Autruche & Compagnie");
        message.setText("Bravo ! Ton envoi d'email via Alwaysdata SMTP fonctionne 🎉");
        mailSender.send(message);
    }

    /**
     * Envoi d'un email de confirmation (utilisé dans MailTestController)
     */
    public void sendConfirmationEmail(String to, String username, String code, LocalDate date) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kakti@alwaysdata.net");
        message.setTo(to);
        message.setSubject("Confirmation d’inscription - Zoo Autruche & Compagnie");
        message.setText("Bonjour " + username + ",\n\n" +
                "Voici ton code de confirmation : " + code +
                "\nDate : " + date +
                "\n\nMerci de ta confiance 🦩 !");
        mailSender.send(message);
    }

    /**
     * Envoi d'un email quand un utilisateur remplit le formulaire de contact
     * (utilisé dans ContactController)
     */
    public void sendContactEmail(Contact contact) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kakti@alwaysdata.net");
        message.setTo("kakti@alwaysdata.net"); // boîte de réception commune
        message.setSubject("📩 Nouveau message de contact");
        message.setText("Nom : " + contact.getName() + "\n" +
                        "Email : " + contact.getEmail() + "\n" +
                        "Message : " + contact.getMessage());
        mailSender.send(message);
    }
    
    /**
     * Envoi d'un email de confirmation de réservation avec numéro de ticket
     */
    public void sendTicketConfirmationEmail(String to, String firstName, String lastName, String ticketNumber, LocalDate visitDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kakti@alwaysdata.net");
        message.setTo(to);
        message.setSubject("🎟 Confirmation de réservation - Zoo Autruche & Compagnie");
        message.setText("Bonjour " + firstName + " " + lastName + ",\n\n" +
                "Merci pour ta réservation au Zoo Autruche & Compagnie 🦩.\n\n" +
                "👉 Ton numéro de ticket est : " + ticketNumber + "\n" +
                "📅 Date de visite : " + visitDate + "\n\n" +
                "Conserve bien ce numéro, il te sera demandé à l’entrée.\n\n" +
                "À très bientôt au Zoo Autruche & Compagnie !");
        mailSender.send(message);
    }

}
