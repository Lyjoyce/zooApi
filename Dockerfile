# Étape 1 : Compilation avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution avec JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Étape 2 : Exécution avec JRE
#FROM eclipse-temurin:21-jre
#WORKDIR /app
#COPY --from=build /app/target/app.jar app.jar
#COPY .env .env
#EXPOSE 8080
#ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
