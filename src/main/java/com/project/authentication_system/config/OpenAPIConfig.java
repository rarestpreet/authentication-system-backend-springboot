package com.project.authentication_system.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Authentication System APIs")
                                .version("v1")
                                .description("API documentation for the Authentication System")
                )
                .servers(
                        List.of(new Server().url("https://authify-n8yi.onrender.com/api/v1").description("production stage"),
                                new Server().url("http://localhost:8080/api/v1").description("development stage"))
                )
                .components(new Components()
                        .addSecuritySchemes("cookieAuth",
                                new SecurityScheme()
                                        .name("jwt")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"))
                .tags(
                        List.of(new Tag().name("Security APIs"),
                                new Tag().name("User APIs"),
                                new Tag().name("Todo APIs"))
                );
    }
}
