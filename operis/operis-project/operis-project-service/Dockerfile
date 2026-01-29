# Utiliser une image de base légère avec OpenJDK 23
FROM eclipse-temurin:23-jre-alpine

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR (généré par mvn clean package)
COPY ./operis-project/operis-project-service/target/operis-project-service-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port de ton service Project
EXPOSE 8089

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]