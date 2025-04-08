FROM amazoncorretto:21

COPY target/*.jar ewm-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ewm-service.war"]