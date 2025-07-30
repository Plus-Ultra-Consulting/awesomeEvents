FROM openjdk:17-jdk-slim
COPY target/*.jar /app/awesomeEvents.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "awesomeEvents.jar"]