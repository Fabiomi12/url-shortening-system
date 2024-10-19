package com.example.url.shortener.controller;

import com.example.url.shortener.dto.ShortenedUrlGetDto;
import com.example.url.shortener.dto.ShortenedUrlPostDto;
import com.example.url.shortener.service.shortener.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping
    @Operation(summary = "Provide a short version of the given url that expires based on given parameters")
    public ShortenedUrlGetDto save(@RequestBody ShortenedUrlPostDto dto) {
        return service.save(dto);
    }

    @GetMapping
    @Operation(summary = "Get all shortened urls belonging to current user")
    public Collection<ShortenedUrlGetDto> findAllCurrentUser() {
        return service.findAllCurrentUser();
    }

    @GetMapping("original")
    @Operation(summary = "Get original url of the provided short url only if it belongs to current user")
    public ShortenedUrlGetDto findOriginalUrl(@RequestParam String shortUrl) {
        return service.findByShortUrlForCurrentUser(shortUrl);
    }
}
