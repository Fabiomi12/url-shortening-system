package com.example.url.shortener.server;

import com.example.url.shortener.config.ApplicationProperties;
import com.example.url.shortener.dal.entity.ShortenedUrl;
import com.example.url.shortener.dal.repo.ShortenedUrlRepository;
import com.example.url.shortener.dto.ShortenedUrlGetDto;
import com.example.url.shortener.dto.ShortenedUrlPostDto;
import com.example.url.shortener.exception.ForbiddenUserActionException;
import com.example.url.shortener.exception.UrlAlreadyExistsException;
import com.example.url.shortener.exception.UrlNotFoundException;
import com.example.url.shortener.mapper.ShortenedUrlMapper;
import com.example.url.shortener.quartz.UrlDeletionScheduler;
import com.example.url.shortener.service.shortener.JpaUrlShortenerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static com.example.url.shortener.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaUrlShortenerServiceTest {

    @Mock
    private ShortenedUrlRepository repository;
    @Mock
    private ShortenedUrlMapper mapper;
    @Mock
    private ApplicationProperties applicationProperties;
    @Mock
    private UrlDeletionScheduler urlDeletionScheduler;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private JpaUrlShortenerService service;

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
    @SneakyThrows
    void save_validUrl_success() {
        ShortenedUrlPostDto postDto = new ShortenedUrlPostDto();
        postDto.setOriginalUrl("https://www.example.com");
        postDto.setExpireTime(7);
        postDto.setExpireTimeUnit(ChronoUnit.DAYS);

        ShortenedUrl entity = new ShortenedUrl(
                "https://www.example.com",
                7,
                ChronoUnit.DAYS
        );
        entity.setId(1L);

        ShortenedUrlGetDto getDto = new ShortenedUrlGetDto();
        getDto.setShortUrl(SHORT_URL);
        getDto.setOriginalUrl(entity.getOriginalUrl());
        getDto.setExpiresAt(entity.getExpiresAt());

        when(repository.findByOriginalUrl(anyString())).thenReturn(Optional.empty());
        when(mapper.fromDto(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toDto(any())).thenReturn(getDto);

        ShortenedUrlGetDto result = service.save(postDto);

        assertEquals(SHORT_URL, result.getShortUrl());
        verify(urlDeletionScheduler).scheduleUrlDeletion(eq(1L), any());
    }

    @Test
    void save_existingUrl_throwsException() {
        ShortenedUrlPostDto postDto = new ShortenedUrlPostDto();
        postDto.setOriginalUrl("https://www.example.com");

        var entity = new ShortenedUrl();
        entity.setId(1L);
        when(repository.findByOriginalUrl(anyString())).thenReturn(Optional.of(entity));

        assertThrows(UrlAlreadyExistsException.class, () -> service.save(postDto));
    }

    @Test
    void findAllCurrentUser_success() {
        ShortenedUrl entity1 = new ShortenedUrl();
        ShortenedUrl entity2 = new ShortenedUrl();
        when(repository.findAllByUserId("testUser")).thenReturn(Arrays.asList(entity1, entity2));
        when(mapper.toDto(any())).thenReturn(new ShortenedUrlGetDto());

        Collection<ShortenedUrlGetDto> results = service.findAllCurrentUser();

        assertEquals(2, results.size());
        verify(repository).findAllByUserId("testUser");
    }

    @Test
    void findEntityByHash_success() {
        ShortenedUrl entity = new ShortenedUrl();
        when(repository.findByHash(HASH)).thenReturn(Optional.of(entity));

        ShortenedUrl result = service.findEntityByHash(HASH);

        assertNotNull(result);
        verify(repository).findByHash(HASH);
    }

    @Test
    void findEntityByHash_notFound_throwsException() {
        when(repository.findByHash(HASH)).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> service.findEntityByHash(HASH));
    }

    @Test
    void findByShortUrlForCurrentUser_success() {
        ShortenedUrl entity = new ShortenedUrl();
        entity.setUserId("testUser");
        when(repository.findByHash(HASH)).thenReturn(Optional.of(entity));
        when(mapper.toDto(any())).thenReturn(new ShortenedUrlGetDto());

        ShortenedUrlGetDto result = service.findByShortUrlForCurrentUser(SHORT_URL);

        assertNotNull(result);
        verify(repository).findByHash(HASH);
    }

    @Test
    void findByShortUrlForCurrentUser_notOwner_throwsException() {
        ShortenedUrl entity = new ShortenedUrl();
        entity.setUserId("otherUser");
        when(repository.findByHash(HASH)).thenReturn(Optional.of(entity));

        assertThrows(ForbiddenUserActionException.class,
                () -> service.findByShortUrlForCurrentUser(SHORT_URL));
    }

    @Test
    void incrementClickCount_success() {
        service.incrementClickCount(1L);
        verify(repository).incrementClickCount(1L);
    }
}