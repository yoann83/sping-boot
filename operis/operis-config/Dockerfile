# Image de base légère avec Java 23
FROM eclipse-temurin:23-jre-alpine

# Répertoire de travail
WORKDIR /app

# Copie du JAR généré par Maven (vérifie bien le nom dans ton dossier target)
COPY ./operis-config/target/operis-config-0.0.1-SNAPSHOT.jar app.jar

# Port d'entrée de la Gateway
EXPOSE 8888

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]