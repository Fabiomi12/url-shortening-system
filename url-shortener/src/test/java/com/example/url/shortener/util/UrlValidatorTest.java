package com.example.url.shortener.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "http://example.com",
        "https://example.com",
        "http://localhost:8080",
        "http://127.0.0.1",
        "https://subdomain.example.com/path",
        "http://example.com/path?param=value",
        "http://example.com/path#fragment"
    })
    void isValidUrl_shouldReturnTrue_forValidUrls(String url) {
        assertTrue(UrlValidator.isValidUrl(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "not_a_url"
    })
    void isValidUrl_shouldReturnFalse_forInvalidUrls(String url) {
        assertFalse(UrlValidator.isValidUrl(url));
    }

    @Test
    void isValidUrl_shouldReturnFalse_forNullUrl() {
        assertFalse(UrlValidator.isValidUrl(null));
    }
}