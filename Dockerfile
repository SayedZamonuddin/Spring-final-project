# Step 1: Use Gradle image to build the app
FROM gradle:8.2.1-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle files and wrapper
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Make the gradlew script executable
RUN chmod +x gradlew

# Download dependencies to cache them
RUN ./gradlew build --no-daemon || exit 0

# Copy source code
COPY src ./src

# Build the project (will generate the JAR file)
RUN ./gradlew bootJar -x test --no-daemon

# Step 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file from the build stage
COPY --from=build /app/build/libs/*.jar demo.jar

# Expose port for the application
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "demo.jar"]
