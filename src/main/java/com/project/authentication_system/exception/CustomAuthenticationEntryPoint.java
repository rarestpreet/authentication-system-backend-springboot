package com.project.authentication_system.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public @NullMarked class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ex) throws IOException, ServletException {
        log.error("Authentication failed for user: {}", ex.getMessage());
        String message = "Login required to access this resource";

        response.setStatus(401);
        response.setContentType("application/json");

        response.getWriter().write("""
                    {
                      "httpStatus": 401,
                      "error": "Unauthorized",
                      "message": "%s"
                    }
                """.formatted(message));
    }
}
