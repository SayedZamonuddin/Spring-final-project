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
RUN ./gradlew build -x test --no-daemon --stacktrace --info || \
    (echo "Gradle build failed. Debugging..." && \
    echo "Inspecting build directory..." && ls -l build && \
    echo "Inspecting build/libs directory..." && ls -l build/libs && \
    echo "Inspecting test reports directory..." && ls -l build/reports/tests/test || true)

# Run the app
CMD ["sh", "-c", "java -jar build/libs/*.jar"]
