package com.example.authorization.server.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@OpenAPIDefinition(info = @Info(title = "Authorization server", version = "v1"))
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(servers())
                .components(new Components()
                        .addSecuritySchemes("oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .clientCredentials(new OAuthFlow()
                                                .tokenUrl("http://localhost:9000/oauth2/token")
                                                .scopes(new Scopes()
                                                        .addString("read", "read scope")
                                                        .addString("write", "write scope")
                                                ))
                                )
                        ))
                .addSecurityItem(new SecurityRequirement().addList("oauth2"));
    }

    private List<Server> servers() {
        var server = new Server();
        server.setUrl("/");
        server.setDescription("Default Server Url");
        return List.of(server);
    }
}
