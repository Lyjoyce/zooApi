# Étape 1 : Compilation avec Maven

FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution avec JRE

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copier uniquement le jar final depuis l'étape 1
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar

# Exposer le port de Spring Boot
EXPOSE 8080

# Pour Render, bonne pratique : configurer un profil actif avec env
ENV SPRING_PROFILES_ACTIVE=dev

# Dossier temporaire (utilisé par Spring Boot)
VOLUME /tmp

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]