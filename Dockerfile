# ===========================
# Stage 1: Build the Application
# ===========================
FROM gradle:8.2.1-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle build files and wrapper scripts first
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Make Gradle Wrapper executable (if using wrapper)
RUN chmod +x gradlew

# Download dependencies (this avoids re-downloading dependencies if nothing changes)
RUN ./gradlew dependencies --no-daemon || exit 0

# Now copy the source code
COPY src ./src

# Build the application and create the executable JAR
RUN ./gradlew bootJar -x test --no-daemon

# ===========================
# Stage 2: Create the Runtime Image
# ===========================
FROM openjdk:17.0.1-jdk-slim

# Set working directory
WORKDIR /app

# Copy the Spring Boot JAR file from the build stage
COPY --from=build /app/build/libs/*.jar demo.jar

# Expose the application port (adjust if your application runs on a different port)
EXPOSE 8080

# Set the entrypoint to run the JAR
ENTRYPOINT ["java", "-jar", "demo.jar"]
