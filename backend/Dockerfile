# Use Maven as a build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set the working directory inside the container for the build
WORKDIR /app

# Copy only pom.xml and resolve dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Now copy the source files
COPY src ./src

# Run the Maven build
RUN mvn clean package -DskipTests

# Use a slim Java runtime for the final stage
FROM openjdk:17-jdk-slim

# Set the working directory inside the container for the runtime
WORKDIR /app

# Add a non-root user for security
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

# Copy the JAR file from the build stage (adjusted path)
COPY --from=build /app/target/*.jar app.jar

# Set environment variables for JVM options
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# Add a health check
HEALTHCHECK --interval=30s --timeout=5s --retries=3 CMD curl --fail http://localhost:8080/api/health || exit 1

# Define the command to run the application
CMD ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "app.jar"]
