package com.example.url.shortener.exception;

public class ForbiddenUserActionException extends ApiException {
    public ForbiddenUserActionException(String message) {
        super(message);
    }
}
