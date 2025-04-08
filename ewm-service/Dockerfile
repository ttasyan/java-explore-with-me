FROM amazoncorretto:21

COPY target/*.war ewm-service.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ewm-service.war"]