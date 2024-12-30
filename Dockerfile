# Use the Eclipse Temurin Alpine official image
FROM eclipse-temurin:21-jdk-alpine

# Install necessary tools
RUN apk add --no-cache bash

# Set working directory
WORKDIR /app

# Copy local code to the container image
COPY . ./

# Ensure Gradle wrapper has execute permissions
RUN chmod +x ./gradlew

# Build the app with Gradle wrapper and disable the daemon
RUN ./gradlew build -x test --no-daemon

# Run the app
CMD ["sh", "-c", "java -jar build/libs/*.jar"]
