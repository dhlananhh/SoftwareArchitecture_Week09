FROM openjdk:17-slim

WORKDIR /app

COPY target/customer-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8084
