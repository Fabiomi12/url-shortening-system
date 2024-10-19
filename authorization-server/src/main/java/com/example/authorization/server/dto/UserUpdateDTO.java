package com.example.authorization.server.dto;

public record UserUpdateDTO(
    String username,
    String firstName,
    String lastName,
    String email,
    String role
) {}