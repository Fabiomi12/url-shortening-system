package com.example.url.shortener.controller;

import com.example.url.shortener.service.redirect.UrlRedirectService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.example.url.shortener.util.Constants.SHORT_URL_PREFIX;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final UrlRedirectService service;

    @GetMapping(SHORT_URL_PREFIX + "{hash}")
    @Operation(summary = "Redirect to log url related to specify hash")
    public void redirectToLongUrl(@PathVariable String hash, HttpServletRequest request, HttpServletResponse response) {
        service.redirect(hash, request, response);
    }
}
