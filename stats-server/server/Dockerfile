FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/*.jar server.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]