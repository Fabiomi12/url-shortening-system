package com.example.authorization.server.service;

import com.example.authorization.server.dal.entity.User;
import com.example.authorization.server.dal.repo.UserRepository;
import com.example.authorization.server.dto.PasswordChangeDTO;
import com.example.authorization.server.dto.UserCreateDTO;
import com.example.authorization.server.dto.UserResponseDTO;
import com.example.authorization.server.dto.UserUpdateDTO;
import com.example.authorization.server.exception.InvalidPasswordException;
import com.example.authorization.server.exception.UserAlreadyExistsException;
import com.example.authorization.server.exception.UserNotFoundException;
import com.example.authorization.server.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserService implements UserService, CustomUserDetailsService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    @Override
    public UserResponseDTO create(UserCreateDTO createDTO) {
        if (userRepository.findByUsername(createDTO.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(createDTO.username());
        user.setPassword(passwordEncoder.encode(createDTO.password()));
        user.setFirstName(createDTO.firstName());
        user.setLastName(createDTO.lastName());
        user.setEmail(createDTO.email());
        user.setRole(createDTO.role());

        return convertToResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO update(UUID id, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getUsername().equals(updateDTO.username()) &&
                userRepository.findByUsername(updateDTO.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        user.setUsername(updateDTO.username());
        user.setFirstName(updateDTO.firstName());
        user.setLastName(updateDTO.lastName());
        user.setEmail(updateDTO.email());
        user.setRole(updateDTO.role());

        return convertToResponseDTO(userRepository.save(user));
    }

    @Override
    public void changePassword(UUID id, PasswordChangeDTO passwordDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(passwordDTO.oldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(passwordDTO.newPassword()));
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO getById(UUID id) {
        return userRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
