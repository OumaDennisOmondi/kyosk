# Use Maven as a build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set the working directory inside the container for the build
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Run the Maven build
RUN mvn clean package -DskipTests

# Use a slim Java runtime for the final stage
FROM openjdk:17-jdk-slim

# Set the working directory inside the container for the runtime
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]