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

# Debug Gradle build step
RUN ./gradlew clean build --no-daemon --stacktrace --info || \
    (echo "Gradle build failed. Debugging..." && \
    echo "Current directory contents:" && ls -l && \
    echo "Build directory contents (if exists):" && ls -l build || true && \
    exit 1)

# Verify JAR file creation
RUN if [ ! -f build/libs/*.jar ]; then \
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
