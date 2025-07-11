# Étape 1 : build de l'application avec Maven + JDK 21 
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier uniquement le pom.xml pour pré-télécharger les dépendances (optimisation)
COPY pom.xml ./

# Pré-télécharger les dépendances Maven (accélère la construction)
RUN mvn dependency:go-offline -B

# Copier le reste du code source dans le conteneur
COPY src ./src

# Compiler et packager l'application sans lancer les tests
RUN mvn clean package -DskipTests -B

# Étape 2 : créer l'image finale d'exécution avec JRE 21 
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copier le jar compilé depuis l'étape builder
COPY --from=builder /app/target/ecorideApi-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port sur lequel tourne ton application Spring Boot
EXPOSE 8081

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
