package com.example.url.shortener.server;

import com.example.url.shortener.dal.entity.ShortenedUrl;
import com.example.url.shortener.exception.ForbiddenUserActionException;
import com.example.url.shortener.exception.UrlNotFoundException;
import com.example.url.shortener.service.redirect.UrlRedirectServiceImpl;
import com.example.url.shortener.service.shortener.UrlShortenerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.url.shortener.Constants.HASH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UrlRedirectServiceImplTest {

    @Mock
    private UrlShortenerService urlShortenerService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private UrlRedirectServiceImpl service;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void redirect_success() {
        ShortenedUrl entity = new ShortenedUrl();
        entity.setId(1L);
        entity.setUserId("testUser");
        entity.setOriginalUrl("https://www.example.com");

        when(urlShortenerService.findEntityByHash(HASH)).thenReturn(entity);

        service.redirect(HASH, request, response);

        assertEquals("https://www.example.com", response.getRedirectedUrl());
        verify(urlShortenerService).incrementClickCount(1L);
    }

    @Test
    void redirect_notOwner_throwsException() {
        ShortenedUrl entity = new ShortenedUrl();
        entity.setUserId("otherUser");

        when(urlShortenerService.findEntityByHash(HASH)).thenReturn(entity);

        assertThrows(ForbiddenUserActionException.class,
            () -> service.redirect(HASH, request, response));
    }

    @Test
    void redirect_notFound_throwsException() {
        when(urlShortenerService.findEntityByHash(HASH)).thenThrow(new UrlNotFoundException(HASH));

        assertThrows(UrlNotFoundException.class,
            () -> service.redirect(HASH, request, response));
    }
}