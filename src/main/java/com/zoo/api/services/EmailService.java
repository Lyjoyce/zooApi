package com.zoo.api.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service gérant l'envoi des emails pour l'application Zoo Autruche & Compagnie.
 * NOTE : Envoi réel bloqué (SMTP Alwaysdata désactivé).
 *        Les appels affichent simplement un message en console.
 */
@Service
public class EmailService {

    /**
     * Test simple d'envoi d'email.
     *
     * @param to adresse du destinataire
     */
    public void sendTestEmail(String to) {
        System.out.println("✅ [SIMULATION] Email de test envoyé à : " + to);
    }

    /**
     * Envoi simulé d'un email de confirmation de ticket.
     *
     * @param to           adresse du destinataire
     * @param firstName    prénom du destinataire
     * @param lastName     nom du destinataire
     * @param ticketNumber numéro du ticket
     * @param visitDate    date de visite
     */
    public void sendTicketConfirmationEmail(String to, String firstName, String lastName,
                                            String ticketNumber, LocalDate visitDate) {
        System.out.println("✅ [SIMULATION] Confirmation envoyée à " + firstName + " " + lastName
                + " | Ticket : " + ticketNumber + " | Date : " + visitDate);
    }

    /**
     * Version simplifiée pour `AdultTicketService` :
     * confirmation sans lastName.
     */
    public void sendConfirmationEmail(String to, String firstName, String ticketNumber, LocalDate visitDate) {
        System.out.println("✅ [SIMULATION] Confirmation envoyée à " + firstName
                + " | Ticket : " + ticketNumber + " | Date : " + visitDate);
    }

    /**
     * Envoi simulé d'un email de contact.
     *
     * @param contact message de contact reçu
     */
    
}
