package com.project.authentication_system.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@NullMarked
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        log.error("Access denied to user: {}", ex.getMessage());
        String message = "You do not have permission to perform this action";

        response.setStatus(403);
        response.setContentType("application/json");

        response.getWriter().write("""
                    {
                      "httpStatus": 403,
                      "error": "Forbidden",
                      "message": "%s"
                    }
                """.formatted(message));
    }
}
