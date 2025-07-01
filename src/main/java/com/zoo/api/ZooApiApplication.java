package com.zoo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ZooApiApplication {

    public static void main(String[] args) {
        // Charge le fichier .env (par défaut à la racine du projet)
        Dotenv dotenv = Dotenv.load();

        // Définit les propriétés système à partir des variables d'environnement du .env
        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("ADMIN_USER", dotenv.get("ADMIN_USER"));
        System.setProperty("ADMIN_PASSWORD", dotenv.get("ADMIN_PASSWORD"));

        // Lancement de l'application Spring Boot
        SpringApplication.run(ZooApiApplication.class, args);
    }
}
