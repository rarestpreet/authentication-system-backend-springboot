package com.project.authentication_system.config;

import com.project.authentication_system.dto.request.RegisterRequestDTO;
import com.project.authentication_system.entity.User;
import com.project.authentication_system.entity.enums.Role;
import com.project.authentication_system.exception.CustomAccessDeniedHandler;
import com.project.authentication_system.exception.CustomAuthenticationEntryPoint;
import com.project.authentication_system.filter.JwtFilter;
import com.project.authentication_system.mapper.UserMapper;
import com.project.authentication_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/auth/login",
                                "/auth/register",
                                "/auth/send-reset-otp",
                                "/auth/reset-password",
                                "/auth/logout",
                                "/test",
                                "/docs",
                                "/docs/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/profile/admin/**", "/todo/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner initData(
            UserRepo userRepo,
            PasswordEncoder encoder
    ) {
        return args -> {
            if (userRepo.findByUsername("admin1").isEmpty()) {
                RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
                registerRequestDTO.setUsername("admin1");
                registerRequestDTO.setPassword(passwordEncoder().encode("admin"));
                registerRequestDTO.setEmail("admin@example.com");

                User admin = UserMapper.toEntity(registerRequestDTO);
                admin.setRoles(new ArrayList<>(List.of(Role.USER, Role.ADMIN)));

                userRepo.save(admin);
            }
        };
    }
}
