package com.example.authorization.server.dto;

import com.example.authorization.server.util.InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String username,
    String firstName,
    String lastName,
    String email,
    String role,
    @JsonSerialize(using = InstantSerializer.class)
    Instant createdAt,
    @JsonSerialize(using = InstantSerializer.class)
    Instant updatedAt
) {}