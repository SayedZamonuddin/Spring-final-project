# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder

# Install necessary tools
RUN apk add --no-cache bash libc6-compat

# Set working directory
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the project files (source code)
COPY src src

# Ensure Gradle wrapper has execute permissions
RUN chmod +x ./gradlew

# Build the application (clean build, verbose, stacktrace for debugging)
RUN ./gradlew clean build --no-daemon --stacktrace --info || \
    (echo "Gradle build failed. Debugging..." && \
    echo "Current directory contents:" && ls -l && \
    echo "Build directory contents (if exists):" && ls -l build && \
    echo "Gradle build logs:" && cat build/reports/build/classes/java/main/*.log || true)

# Verify JAR file creation
RUN if [ ! -f build/libs/*.jar ]; then \
    echo "JAR file not found in build/libs. Build might have failed."; \
    exit 1; \
fi

# Stage 2: Run
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
