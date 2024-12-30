# ===========================
# Stage 1: Build the Application
# ===========================
FROM gradle:8.2.1-jdk17 AS build

WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || return 0

COPY src ./src

# Build the JAR file with logs on failure
RUN ./gradlew bootJar -x test --no-daemon || (cat /app/build/reports/*/* && exit 1)

# ===========================
# Stage 2: Create the Runtime Image
# ===========================
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
