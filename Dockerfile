## Start with a base image that has JDK 17 installed
#FROM maven:3.8.4-openjdk-17-slim AS build
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the Maven project descriptor files to cache dependencies
#COPY pom.xml .
#COPY src ./src
#
## Build the application and package it into a JAR (skip tests during build)
#RUN mvn package -DskipTests
#
## Create a new stage for the final Docker image
#FROM openjdk:17-jdk-slim
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the JAR file and resources from the Maven build stage
#COPY --from=build /app/target/HotelApp-3.2.5.jar app.jar
#COPY --from=build /app/target/classes /app/classes
#
## Add wait-for-it script
#ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
#RUN chmod +x /wait-for-it.sh
#
## Copy the custom entrypoint script
#COPY entrypoint.sh /entrypoint.sh
#RUN chmod +x /entrypoint.sh
#CMD ["./wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "app.jar"]
## Define the entry point for the application
#ENTRYPOINT ["/entrypoint.sh"]

FROM openjdk:17-jdk-slim

COPY target/HotelApp-3.2.5.jar app.jar

RUN mkdir -p /app/com/HotelApp/util/encryptionUtil/keys

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
