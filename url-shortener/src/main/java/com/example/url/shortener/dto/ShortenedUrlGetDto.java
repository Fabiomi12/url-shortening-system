package com.example.url.shortener.dto;

import com.example.url.shortener.util.InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ShortenedUrlGetDto {
    private String shortUrl;
    private String originalUrl;
    @JsonSerialize(using = InstantSerializer.class)
    private Instant expiresAt;
    private Long clickCount;
}
