package com.example.url.shortener.dal.entity;

import com.example.url.shortener.util.RequestUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "shortened_url", indexes = {@Index(columnList = "hash")})
@Getter
@Setter
@NoArgsConstructor
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Default expire time in two hours
    private Instant expiresAt = Instant.now().plus(2, ChronoUnit.HOURS);
    private String originalUrl;
    private String hash;
    private String userId;
    private long clickCount = 0L;

    public ShortenedUrl(String originalUrl, int expireTime, ChronoUnit expireTimeUnit) {
        this.originalUrl = originalUrl;
        this.expiresAt = Instant.now().plus(expireTime, expireTimeUnit);
        this.userId = RequestUtils.getCurrentAuthentication().getName();
    }
}
