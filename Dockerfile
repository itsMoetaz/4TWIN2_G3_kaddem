# Étape 1 : utiliser une image Java légère
FROM openjdk:17-jdk-slim

# Variable d'environnement
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

# Créer un dossier dans le container
WORKDIR /app

# Copier le JAR compilé
COPY target/kaddem-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port utilisé par Spring Boot
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
