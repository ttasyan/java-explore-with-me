FROM openjdk:21-jdk-slim

COPY target/ewm-service-0.0.1-SNAPSHOT.jar ewm-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ewm-service.jar"]