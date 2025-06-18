# awesomeEvents

## Getting started

1. Create and configure file at `src/main/resources/application.properties` (Use template at `src/main/resources/example.properties`)

2. Run:
```bash
# Start the Database
docker compose up -d

# Build JAR
./mvnw clean package

# Run JAR
java -jar target/ARTawesomeEvents-*.jar
```

API is available at `http://localhost:8080/`
