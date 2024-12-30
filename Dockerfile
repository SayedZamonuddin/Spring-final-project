# Use the Eclipse Temurin Alpine official image
FROM eclipse-temurin:21-jdk-alpine

# Install Gradle
RUN apk add --no-cache gradle bash

# Set working directory
WORKDIR /app

# Copy local code to the container image
COPY . ./

# Ensure Gradle wrapper has execute permissions (if using wrapper)
RUN chmod +x ./gradlew

# Build the app with Gradle wrapper (if available) or Gradle
RUN ./gradlew build -x test || gradle build -x test

# Run the app
CMD ["sh", "-c", "java -jar build/libs/*.jar"]
