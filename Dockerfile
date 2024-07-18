FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/HotelApp-3.2.5.jar app.jar
COPY target/classes/ /app/
ENTRYPOINT ["java", "-jar", "app.jar"]
