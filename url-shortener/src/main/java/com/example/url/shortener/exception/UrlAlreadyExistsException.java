package com.example.url.shortener.exception;

public class UrlAlreadyExistsException extends ApiException {
    public UrlAlreadyExistsException() {
        super("An shortened url already exists that points to provided long url.");
    }
}
