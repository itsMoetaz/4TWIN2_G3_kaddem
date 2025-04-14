FROM openjdk:8-jdk-alpine
EXPOSE 8082
ADD http://192.168.33.10:8081/repository/maven-snapshots/tn/esprit/spring/kaddem/0.0.1-SNAPSHOT/kaddem-0.0.1-20250414.180100-1.jar Malekswissi4twin2-1.0.jar
ENTRYPOINT ["java","-jar","/Malekswissi4twin2-1.0.jar"]
