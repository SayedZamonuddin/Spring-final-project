# Use the Eclipse Temurin Alpine official image
FROM eclipse-temurin:21-jdk-alpine

# Install necessary tools
RUN apk add --no-cache bash libc6-compat

# Set working directory
WORKDIR /app

# Copy local code to the container image
COPY . ./

# Ensure Gradle wrapper has execute permissions
RUN chmod +x ./gradlew

# Debug Gradle build
RUN ./gradlew build --no-daemon --stacktrace --info || \
    (echo "Gradle build failed. Debugging..." && \
    echo "Inspecting current directory..." && ls -l && \
    echo "Inspecting build directory (if exists)..." && ls -l build || true)

# Ensure the JAR file exists
RUN if [ ! -f build/libs/*.jar ]; then \
    echo "JAR file not found in build/libs. Build might have failed."; \
    exit 1; \
fi

# Run the app
CMD ["sh", "-c", "java -jar build/libs/*.jar"]
