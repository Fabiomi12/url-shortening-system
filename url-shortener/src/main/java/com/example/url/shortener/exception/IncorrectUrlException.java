package com.example.url.shortener.exception;

public class IncorrectUrlException extends ApiException {
    public IncorrectUrlException() {
        super("Provided URL is not a correct one.");
    }
}
