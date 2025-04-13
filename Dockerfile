FROM openjdk:17-jdk-alpine
LABEL author="Moetaz Ben Khedher"
EXPOSE 8089
RUN apk add --no-cache curl
ARG JAR_FILE_URL=http://192.167.33.10:8087/repository/maven-releases/tn/esprit/spring/4TWIN2-G3-kaddem/0.0.1-SNAPSHOT/4TWIN2-G3-kaddem-0.0.1-20250413.XXXXXX-1.jar
RUN curl -u "admin:admin" -L $JAR_FILE_URL -o app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
