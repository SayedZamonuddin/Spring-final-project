# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle

# Copy the application source code
COPY src src

# Add necessary permissions to the Gradle wrapper
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build -x test

# Copy the built jar to a new stage
FROM eclipse-temurin:17-jre-alpine

# Set environment variables for the container
ENV SPRING_PROFILES_ACTIVE=prod

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=0 /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
