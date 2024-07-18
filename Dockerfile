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

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage to the current working directory
COPY --from=build /app/target/HotelApp-3.2.5.jar app.jar

# Add wait-for-it script
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Add entrypoint script that waits for MySQL to start
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Expose the port your app runs on
EXPOSE 8080

# Define the entrypoint script to be executed when the container starts
ENTRYPOINT ["/entrypoint.sh"]

# Specify the command to run on container startup (if entrypoint is not used)
# CMD ["java", "-jar", "app.jar"]
