FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar crud-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "crud-api.jar"]