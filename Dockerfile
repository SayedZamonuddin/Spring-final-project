# ===========================
# Stage 1: Build the Application
# ===========================
FROM gradle:8.2.1-jdk17 AS build

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and configuration files first
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

# Make Gradle wrapper executable
RUN chmod +x gradlew

# Pre-fetch dependencies to leverage Docker caching
RUN ./gradlew dependencies --no-daemon || return 0

# Copy the application source code
COPY src ./src

# Build the Spring Boot application JAR file
RUN ./gradlew bootJar -x test --no-daemon

# ===========================
# Stage 2: Create the Runtime Image
# ===========================
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the generated JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port (adjust if your Spring Boot app uses a different port)
EXPOSE 8080

# Set the default command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
