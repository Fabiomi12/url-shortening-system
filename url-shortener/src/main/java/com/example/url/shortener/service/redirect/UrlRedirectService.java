package com.example.url.shortener.service.redirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UrlRedirectService {
    void redirect(String hash, HttpServletRequest request, HttpServletResponse response);
}
