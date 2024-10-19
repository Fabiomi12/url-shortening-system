package com.example.url.shortener.util;

import java.util.regex.Pattern;

public class UrlValidator {
    private UrlValidator() {}

    private static final String URL_REGEX = "^(https?://)"  // protocol
            + "((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|"  // domain name
            + "localhost|"  // localhost
            + "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})"  // IP address
            + "(:\\d+)?"  // port
            + "(/[-a-z\\d%_.~+]*)*"  // path
            + "(\\?[;&a-z\\d%_.~+=-]*)?"  // query string
            + "(#.*)?$";  // fragment identifier



    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

    public static boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        return URL_PATTERN.matcher(url).matches();
    }
}
