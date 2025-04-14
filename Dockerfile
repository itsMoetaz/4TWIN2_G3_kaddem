# Use official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file from Maven build
COPY target/kaddem-0.0.1-SNAPSHOT.jar app.jar

# Expose port (adjust if different)
EXPOSE 8089

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
