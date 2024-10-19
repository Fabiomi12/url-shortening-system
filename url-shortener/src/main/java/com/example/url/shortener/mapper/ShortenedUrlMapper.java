package com.example.url.shortener.mapper;

import com.example.url.shortener.config.ApplicationProperties;
import com.example.url.shortener.dal.entity.ShortenedUrl;
import com.example.url.shortener.dto.ShortenedUrlGetDto;
import com.example.url.shortener.dto.ShortenedUrlPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.url.shortener.util.Constants.SHORT_URL_PREFIX;

@Component
@RequiredArgsConstructor
public class ShortenedUrlMapper {

    private final ApplicationProperties applicationProperties;

    public ShortenedUrl fromDto(ShortenedUrlPostDto dto) {
        return new ShortenedUrl(
                dto.getOriginalUrl(),
                dto.getExpireTime(),
                dto.getExpireTimeUnit()
        );
    }

    public ShortenedUrlGetDto toDto(ShortenedUrl entity) {
        var dto = new ShortenedUrlGetDto();
        dto.setOriginalUrl(entity.getOriginalUrl());
        dto.setShortUrl(applicationProperties.getApplicationUrl() + SHORT_URL_PREFIX + entity.getHash());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setClickCount(entity.getClickCount());
        return dto;
    }
}
