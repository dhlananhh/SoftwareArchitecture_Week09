@echo off
echo Building the project with Maven in Docker...

docker run --rm -v %cd%:/app -w /app maven:3.9.9-openjdk:latest mvn clean package -DskipTests

echo Build completed. Now you can run docker-compose up -d.
