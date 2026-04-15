package com.project.authentication_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class WebMvcConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = getCorsConfig();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    private CorsConfiguration getCorsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration
                .setAllowedOrigins(List.of("http://localhost:5173", "https://authify-n8yi.onrender.com"));
        corsConfiguration
                .setAllowedMethods(List.of("PUT", "POST", "GET", "DELETE", "OPTIONS"));
        corsConfiguration
                .setAllowedHeaders(List.of("*"));
        corsConfiguration
                .setMaxAge(3600L);
        corsConfiguration
                .setAllowCredentials(true);

        return corsConfiguration;
    }
}
