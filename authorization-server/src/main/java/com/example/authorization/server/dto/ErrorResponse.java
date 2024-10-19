package com.example.authorization.server.dto;

import com.example.authorization.server.util.InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;

public record ErrorResponse(
    @JsonSerialize(using = InstantSerializer.class)
    Instant timestamp,
    int status,
    String error,
    String message
) {}