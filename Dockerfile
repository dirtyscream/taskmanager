# Stage 1: Build the application with Maven
FROM openjdk:17-slim AS builder

RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve
COPY src src
RUN ./mvnw package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-slim
WORKDIR /app
RUN mkdir -p /app/logs
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
