FROM openjdk:21-jdk-slim

COPY target/server.jar server.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "/server.jar"]