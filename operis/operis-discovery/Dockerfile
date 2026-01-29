# Utiliser une image de base légère avec OpenJDK 23
FROM eclipse-temurin:23-jre-alpine

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR construit dans le conteneur
COPY ./operis-discovery/target/operis-discovery-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port 8761 pour le service Eureka
EXPOSE 8761

# Définir le point d'entrée pour exécuter l'application Java
ENTRYPOINT ["java", "-jar", "app.jar"]