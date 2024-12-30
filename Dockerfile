# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the build.gradle and settings files into the container
COPY build.gradle settings.gradle ./

# Copy the source code into the container
COPY src ./src

# Build the application using Gradle
RUN ./gradlew build --no-daemon

# Use a smaller image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the build image
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port your application will run on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
