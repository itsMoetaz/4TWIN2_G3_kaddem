FROM openjdk:8-jdk-alpine
EXPOSE 8089
ADD http://192.168.56.10:8081/repository/maven-releases/tn/esprit/spring/kaddem/1.0.0/kaddem-1.0.0.jar /kaddem.jar
ENTRYPOINT ["java", "-jar", "/kaddem.jar"]
