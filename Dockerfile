# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy only the POM file to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the application
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the final Docker image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file from the previous stage
COPY --from=build /app/target/HotelApp-3.2.5.jar app.jar

# Specify the command to run on container startup
CMD ["java", "-jar", "app.jar"]
