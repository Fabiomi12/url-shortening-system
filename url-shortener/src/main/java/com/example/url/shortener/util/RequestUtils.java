package com.example.url.shortener.util;

import com.example.url.shortener.exception.NoAuthenticationException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RequestUtils {
    private RequestUtils() {}

    @NonNull
    public static Authentication getCurrentAuthentication() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new NoAuthenticationException();
        return authentication;
    }
}
