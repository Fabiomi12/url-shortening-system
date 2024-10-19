package com.example.authorization.server.config;

import com.example.authorization.server.dal.repo.UserRepository;
import com.example.authorization.server.dto.UserCreateDTO;
import com.example.authorization.server.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    CommandLineRunner initDatabase(UserService userService, UserRepository userRepository) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            UserCreateDTO admin = new UserCreateDTO(
                "fabio",
                "password",
                "fabio",
                "User",
                "fabio@example.com",
                "ADMIN"
            );

            UserCreateDTO user = new UserCreateDTO(
                "user",
                "password",
                "Regular",
                "User",
                "user@example.com",
                "USER"
            );

            userService.create(admin);
            userService.create(user);
        };
    }
}
