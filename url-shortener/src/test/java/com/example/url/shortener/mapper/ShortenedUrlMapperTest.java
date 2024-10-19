package com.example.url.shortener.mapper;

import com.example.url.shortener.config.ApplicationProperties;
import com.example.url.shortener.dal.entity.ShortenedUrl;
import com.example.url.shortener.dto.ShortenedUrlGetDto;
import com.example.url.shortener.dto.ShortenedUrlPostDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.example.url.shortener.Constants.*;
import static com.example.url.shortener.util.Constants.SHORT_URL_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ShortenedUrlMapperTest {

    @Mock
    private ApplicationProperties applicationProperties;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private ShortenedUrlMapper mapper;


    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(applicationProperties.getApplicationUrl()).thenReturn(APPLICATION_URL);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void fromDto_shouldMapAllFields() {
        // Given
        ShortenedUrlPostDto dto = new ShortenedUrlPostDto();
        dto.setOriginalUrl(EXAMPLE_URL);
        dto.setExpireTime(5);
        dto.setExpireTimeUnit(ChronoUnit.DAYS);

        // When
        ShortenedUrl entity = mapper.fromDto(dto);

        // Then
        assertNotNull(entity);
        assertEquals(dto.getOriginalUrl(), entity.getOriginalUrl());
        assertTrue(entity.getExpiresAt().isAfter(Instant.now()));
        assertTrue(entity.getExpiresAt().isBefore(Instant.now().plus(5, ChronoUnit.DAYS).plusSeconds(1)));
    }

    @Test
    void fromDto_withDefaultValues_shouldSetDefaultExpiration() {
        // Given
        ShortenedUrlPostDto dto = new ShortenedUrlPostDto();
        dto.setOriginalUrl(EXAMPLE_URL);
        // ExpireTime and ExpireTimeUnit are not set, should use defaults

        // When
        ShortenedUrl entity = mapper.fromDto(dto);

        // Then
        assertNotNull(entity);
        assertEquals(dto.getOriginalUrl(), entity.getOriginalUrl());
        assertTrue(entity.getExpiresAt().isAfter(Instant.now()));
        assertTrue(entity.getExpiresAt().isBefore(Instant.now().plus(2, ChronoUnit.HOURS).plusSeconds(1)));
    }

    @Test
    void toDto_shouldMapAllFields() {
        // Given
        ShortenedUrl entity = new ShortenedUrl();
        entity.setOriginalUrl(EXAMPLE_URL);
        entity.setHash(HASH);
        entity.setExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS));
        entity.setClickCount(5L);

        // When
        ShortenedUrlGetDto dto = mapper.toDto(entity);

        // Then
        assertEquals(entity.getOriginalUrl(), dto.getOriginalUrl());
        assertEquals(SHORT_URL, dto.getShortUrl());
        assertEquals(entity.getExpiresAt(), dto.getExpiresAt());
        assertEquals(entity.getClickCount(), dto.getClickCount());
    }

    @Test
    void toDto_withNullFields_shouldHandleGracefully() {
        // Given
        ShortenedUrl entity = new ShortenedUrl();
        // All fields are null

        // When
        ShortenedUrlGetDto dto = mapper.toDto(entity);

        // Then
        assertNull(dto.getOriginalUrl());
        assertEquals("http://localhost:8080" + SHORT_URL_PREFIX + "null", dto.getShortUrl());
        assertNotNull(dto.getExpiresAt());
        assertEquals(0L, dto.getClickCount());
    }
}