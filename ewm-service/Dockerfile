FROM openjdk:21-jdk-slim

COPY target/*.jar ewm-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ewm-service.war"]