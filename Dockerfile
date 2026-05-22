FROM eclipse-temurin:17-jdk
COPY target/*.jar /app/awesomeEvents.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "awesomeEvents.jar"]