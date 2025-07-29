package com.zoo.api.services;

import com.zoo.api.documents.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

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
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendContactEmail(Contact contact) {
        if (!mailEnabled) {
            System.out.println("Mail sending is disabled. Skipping contact email.");
            return;
        }

        try {
            MailjetClient client = new MailjetClient(apiKey, apiSecret);

            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                    .put(new JSONObject()
                        .put(Emailv31.Message.FROM, new JSONObject()
                            .put("Email", "autruchecompagnie@gmail.com")
                            .put("Name", "Autruche & Compagnie"))
                        .put(Emailv31.Message.TO, new JSONArray()
                            .put(new JSONObject()
                                .put("Email", "autruchecompagnie@gmail.com")
                                .put("Name", "Autruche & Compagnie")))
                        .put(Emailv31.Message.SUBJECT, contact.getSubject())
                        .put(Emailv31.Message.TEXTPART, contact.getMessage())
                        .put(Emailv31.Message.HTMLPART, "<p><strong>Message de :</strong> " + contact.getName()
                            + " (" + contact.getEmail() + ")<br><br>" + contact.getMessage() + "</p>")
                    )
                );

            MailjetResponse response = client.post(request);
            System.out.println("Status: " + response.getStatus());
            System.out.println("Data: " + response.getData());

        } catch (Exception e) {
            System.err.println("Erreur lors de l’envoi du mail via Mailjet :");
            e.printStackTrace();
        }

    }
}
