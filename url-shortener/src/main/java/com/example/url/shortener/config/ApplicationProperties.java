package com.example.url.shortener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "url.shortener")
@Getter
@Setter
public class ApplicationProperties {
    private String applicationUrl;
}
