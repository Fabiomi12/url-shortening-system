# URL Shortener with OAuth2 (Code Challenge)

A multi-module Spring Boot application consisting of:
- Authorization Server: OAuth2 authentication service
- URL Shortener: URL shortening service with OAuth2 authentication

## Tech Stack

- Java 21
- Spring Boot 3.3.4
- Spring Security with OAuth2
- SpringDoc OpenAPI
- PostgreSQL
- Lombok
- Gradle (Multi-module project)

## Configuration

1. Database Setup
```sql
CREATE DATABASE authorization_server;
CREATE DATABASE url_shortener;
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
