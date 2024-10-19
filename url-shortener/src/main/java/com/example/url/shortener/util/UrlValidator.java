package com.example.url.shortener.util;

import java.net.URI;

public class UrlValidator {
    private UrlValidator() {}

    public static boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            new URI(url).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
