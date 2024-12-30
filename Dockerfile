# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

# Install necessary tools
RUN apk add --no-cache bash libc6-compat

# Set working directory
WORKDIR /app

# Copy project files to the container
COPY . ./

# Ensure Gradle wrapper has execute permissions
RUN chmod +x ./gradlew

# Build the application and verify the JAR file
RUN ./gradlew build --no-daemon --stacktrace --info && \
    echo "Checking for generated JAR in build/libs..." && ls -l build/libs && \
    if [ ! -f build/libs/*.jar ]; then \
        echo "JAR file not found in build/libs. Build might have failed."; \
        exit 1; \
    fi

# Stage 2: Run
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
