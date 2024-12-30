# Use a base image with OpenJDK
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and wrapper properties
COPY gradlew gradlew.bat gradle/ ./

# Make the gradlew script executable
RUN chmod +x gradlew

# Copy the entire project into the container
COPY . .

# Run Gradle build (use the wrapper to ensure correct version of Gradle)
RUN ./gradlew build --no-daemon

# Use a smaller base image to run the application
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the previous build stage
COPY --from=0 /app/build/libs/your-app-name.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
