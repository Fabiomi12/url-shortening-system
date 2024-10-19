# URL Shortener with OAuth2

A multi-module Spring Boot application consisting of:
- Authorization Server: OAuth2 authentication service
- URL Shortener: URL shortening service with OAuth2 authentication

## Prerequisites

- Java 21
- Gradle
- PostgreSQL

## Configuration

1. Create two PostgreSQL databases:
```bash
createdb authorization_server
createdb url_shortener
```

2. Configure database connections in each module's `application.properties`:
    - `authorization-server/src/main/resources/application.properties`
    - `url-shortener/src/main/resources/application.properties`

## Running the Application

```bash
# Terminal 1
./gradlew :authorization-server:bootRun

# Terminal 2 (after auth server is running)
./gradlew :url-shortener:bootRun
```

## API Documentation

- Authorization Server Swagger UI: http://localhost:9000/swagger-ui/index.html
- URL Shortener Swagger UI: http://localhost:8080/swagger-ui/index.html

## Testing

```bash
./gradlew test
```