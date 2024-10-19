package com.example.authorization.server.dto;

public record PasswordChangeDTO(
    String oldPassword,
    String newPassword
) {}