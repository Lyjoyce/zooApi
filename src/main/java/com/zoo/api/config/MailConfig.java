package com.zoo.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class MailConfig {

    @Bean
    public MailSender mailSender() {
        return new MailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) {
                // rien à faire
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) {
                // rien à faire
            }
        };
    }
}
