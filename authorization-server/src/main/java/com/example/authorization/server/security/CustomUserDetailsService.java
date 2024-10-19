package com.example.authorization.server.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface CustomUserDetailsService extends UserDetailsService {
    PasswordEncoder getPasswordEncoder();
}
