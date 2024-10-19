package com.example.url.shortener.exception;

import com.example.url.shortener.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    void handleGlobalException() {
        Exception exception = new Exception("Test exception");
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An unexpected error occurred", responseEntity.getBody().getMessage());
        assertEquals(500, responseEntity.getBody().getStatus());
    }

    @Test
    void handleUrlNotFoundException() {
        UrlNotFoundException exception = new UrlNotFoundException("test");
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleUrlNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No URL found for hash: test", responseEntity.getBody().getMessage());
        assertEquals(404, responseEntity.getBody().getStatus());
    }

    @Test
    void handleForbiddenUserAction() {
        ForbiddenUserActionException exception = new ForbiddenUserActionException("Forbidden action");
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleForbiddenUserAction(exception);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Forbidden action", responseEntity.getBody().getMessage());
        assertEquals(403, responseEntity.getBody().getStatus());
    }

    @Test
    void handleUrlAlreadyExistsException() {
        UrlAlreadyExistsException exception = new UrlAlreadyExistsException();
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleUrlAlreadyExistsException(exception);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("An shortened url already exists that points to provided long url.", responseEntity.getBody().getMessage());
        assertEquals(409, responseEntity.getBody().getStatus());
    }

    @Test
    void handleApiException() {
        ApiException exception = new ApiException("API error");
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleApiException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("API error", responseEntity.getBody().getMessage());
        assertEquals(400, responseEntity.getBody().getStatus());
    }
}