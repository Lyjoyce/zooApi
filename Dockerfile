# Étape 1 : Compilation avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution avec JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/zooApi.jar app.jar
COPY .env .env
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
