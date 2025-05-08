# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy your Maven project files into the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the built jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port (adjust if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
