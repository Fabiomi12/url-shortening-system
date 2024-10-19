package com.example.url.shortener.exception;

public class NoAuthenticationException extends ApiException {
    public NoAuthenticationException() {
        super("No authentication provided");
    }
}
