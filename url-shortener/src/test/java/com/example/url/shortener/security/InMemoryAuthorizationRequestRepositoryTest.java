package com.example.url.shortener.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAuthorizationRequestRepositoryTest {

    private InMemoryAuthorizationRequestRepository repository;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAuthorizationRequestRepository();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void loadAuthorizationRequest_shouldReturnStoredRequest() {
        // Given
        String state = "test-state";
        request.setParameter("state", state);
        OAuth2AuthorizationRequest authRequest = createTestAuthRequest(state);
        
        repository.saveAuthorizationRequest(authRequest, request, response);
        
        // When
        OAuth2AuthorizationRequest result = repository.loadAuthorizationRequest(request);
        
        // Then
        assertEquals(authRequest, result);
    }

    @Test
    void saveAuthorizationRequest_shouldStoreRequest() {
        // Given
        String state = "test-state";
        OAuth2AuthorizationRequest authRequest = createTestAuthRequest(state);
        
        // When
        repository.saveAuthorizationRequest(authRequest, request, response);
        
        // Then
        request.setParameter("state", state);
        OAuth2AuthorizationRequest result = repository.loadAuthorizationRequest(request);
        assertEquals(authRequest, result);
    }

    @Test
    void removeAuthorizationRequest_shouldRemoveAndReturnStoredRequest() {
        // Given
        String state = "test-state";
        request.setParameter("state", state);
        OAuth2AuthorizationRequest authRequest = createTestAuthRequest(state);
        
        repository.saveAuthorizationRequest(authRequest, request, response);
        
        // When
        OAuth2AuthorizationRequest removedRequest = repository.removeAuthorizationRequest(request, response);
        
        // Then
        assertEquals(authRequest, removedRequest);
        assertNull(repository.loadAuthorizationRequest(request));
    }

    private OAuth2AuthorizationRequest createTestAuthRequest(String state) {
        return OAuth2AuthorizationRequest.authorizationCode()
                .clientId("client")
                .authorizationUri("http://localhost:9000")
                .redirectUri("http://localhost:8080")
                .state(state)
                .build();
    }
}