package com.example.url.shortener.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser
    @Test
    public void testProtectedEndpointIsAccessibleWithAuth() throws Exception {
        mockMvc.perform(get("/api/v1/urls"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginPageIsAccessible() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/url-shortener"))
                .andExpect(status().is3xxRedirection());
    }
}
