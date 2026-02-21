package com.project.authentication_system.filter;

import com.project.authentication_system.service.JWTService;
import com.project.authentication_system.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public @NullMarked class JwtFilter extends OncePerRequestFilter {

    private final UserDetailService userDetailService;
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token = authHeader.replace("Bearer ", "");
            email = jwtService.extractEmail(token);
        }

        if(email!=null &&
                SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails = userDetailService.loadUserByUsername(email);

            if(jwtService.isTokenValid(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        doFilter(request, response, filterChain);
    }
}
