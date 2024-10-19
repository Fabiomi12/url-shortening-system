package com.example.url.shortener.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
