FROM maven:3.8.7-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR target/kaddem-0.0.1-SNAPSHOT
COPY --from=build /app/target/kaddem-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "kaddem-0.0.1-SNAPSHOT.jar"]