package com.example.url.shortener.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class ShortenedUrlPostDto {
    private String originalUrl;
    private Integer expireTime = 2;
    private ChronoUnit expireTimeUnit = ChronoUnit.HOURS;
}
