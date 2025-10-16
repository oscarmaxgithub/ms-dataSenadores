
# Etapa 1: Compilación con Maven
FROM maven:3.8.5-openjdk-17 AS build
LABEL authors="oscar"

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Creación de la imagen final ligera
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copiamos el JAR compilado desde la etapa de build
COPY --from=build /app/target/demo-ci-cd-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]