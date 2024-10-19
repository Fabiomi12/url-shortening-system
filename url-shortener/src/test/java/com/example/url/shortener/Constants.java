package com.example.url.shortener;

import java.util.Base64;

import static com.example.url.shortener.util.Constants.SHORT_URL_PREFIX;

public interface Constants {
    String HASH = Base64.getUrlEncoder().withoutPadding().encodeToString("1".getBytes());
    String APPLICATION_URL = "http://localhost:8080";
    String SHORT_URL = APPLICATION_URL + SHORT_URL_PREFIX + HASH;
    String EXAMPLE_URL = "https://example.com";
}
