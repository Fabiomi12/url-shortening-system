package com.example.authorization.server.service;

import com.example.authorization.server.dto.PasswordChangeDTO;
import com.example.authorization.server.dto.UserCreateDTO;
import com.example.authorization.server.dto.UserResponseDTO;
import com.example.authorization.server.dto.UserUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDTO create(UserCreateDTO createDTO);
    UserResponseDTO getById(UUID id);
    UserResponseDTO update(UUID id, UserUpdateDTO updateDTO);
    void delete(UUID id);
    List<UserResponseDTO> getAll();
    void changePassword(UUID id, PasswordChangeDTO passwordDTO);
}