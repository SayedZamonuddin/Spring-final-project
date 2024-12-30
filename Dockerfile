# Use the Eclipse Temurin Alpine official image
FROM eclipse-temurin:21-jdk-alpine

# Install Gradle
RUN apk add --no-cache gradle

# Set working directory
WORKDIR /app

# Copy local code to the container image
COPY . ./

# Build the app with Gradle (you can add any specific tasks or properties as needed)
RUN gradle build -x test

# Run the app
CMD ["sh", "-c", "java -jar build/libs/*.jar"]
