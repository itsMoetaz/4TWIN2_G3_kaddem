FROM openjdk:17-jdk-alpine
LABEL author="Moetaz Ben Khedher"
EXPOSE 8089
RUN apk add --no-cache curl
ADD http://192.167.33.10:8081/repository/maven-public/tn/esprit/spring/services/4TWIN2-G3-kaddem/1.0/4TWIN2-G3-kaddem-1.0.jar 4TWIN2-G3-kaddem-1.0.jar
ENTRYPOINT ["java","-jar","/4TWIN2-G3-kaddem-1.0.jar"]
