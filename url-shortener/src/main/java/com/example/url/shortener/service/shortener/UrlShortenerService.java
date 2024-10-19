package com.example.url.shortener.service.shortener;

import com.example.url.shortener.dal.entity.ShortenedUrl;
import com.example.url.shortener.dto.ShortenedUrlGetDto;
import com.example.url.shortener.dto.ShortenedUrlPostDto;

import java.util.Collection;

public interface UrlShortenerService {
    ShortenedUrlGetDto save(ShortenedUrlPostDto dto);
    Collection<ShortenedUrlGetDto> findAllCurrentUser();
    ShortenedUrl findEntityByHash(String hash);
    ShortenedUrlGetDto findByShortUrlForCurrentUser(String shortUrl);
    void incrementClickCount(long id);
}
