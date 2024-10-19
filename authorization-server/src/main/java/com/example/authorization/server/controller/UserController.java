package com.example.authorization.server.controller;

import com.example.authorization.server.dto.PasswordChangeDTO;
import com.example.authorization.server.dto.UserCreateDTO;
import com.example.authorization.server.dto.UserResponseDTO;
import com.example.authorization.server.dto.UserUpdateDTO;
import com.example.authorization.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(createDTO));
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Update user details")
    @PutMapping("{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateDTO updateDTO) {
        return ResponseEntity.ok(userService.update(id, updateDTO));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Change user password")
    @PostMapping("{id}/password")
    public ResponseEntity<Void> changePassword(            @PathVariable UUID id,
            @RequestBody PasswordChangeDTO passwordDTO) {
        userService.changePassword(id, passwordDTO);
        return ResponseEntity.noContent().build();
    }
}
