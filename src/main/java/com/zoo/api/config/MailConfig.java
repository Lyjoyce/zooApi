package com.zoo.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // Bean vide pour prod : ne fait rien
        return new JavaMailSender() {
            @Override public void send(org.springframework.mail.SimpleMailMessage simpleMessage) {}
            @Override public void send(org.springframework.mail.SimpleMailMessage... simpleMessages) {}
            @Override public void send(MimeMessage mimeMessage) {}
            @Override public void send(MimeMessage... mimeMessages) {}
            @Override public MimeMessage createMimeMessage() { return null; }
            @Override public MimeMessage createMimeMessage(java.io.InputStream contentStream) { return null; }
        };
    }
}
