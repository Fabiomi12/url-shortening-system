package com.example.url.shortener.dto;

import com.example.url.shortener.util.InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    @JsonSerialize(using = InstantSerializer.class)
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
}
