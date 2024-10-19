package com.example.authorization.server.dto;

public record UserCreateDTO(
    String username,
    String password,
    String firstName,
    String lastName,
    String email,
    String role
) {}