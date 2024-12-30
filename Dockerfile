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
    cat build/reports/tests/test/index.html

# Run the app
CMD ["sh", "-c", "java -jar build/libs/*.jar"]
