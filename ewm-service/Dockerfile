FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/*.jar ewm-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]