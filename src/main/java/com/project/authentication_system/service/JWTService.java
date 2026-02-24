package com.project.authentication_system.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTService {

    private final SecretKey secretkey;

    public JWTService(@Value("${jwt.secret}")  String secretkey) {
        this.secretkey = Keys.hmacShaKeyFor(secretkey.getBytes());
    }
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ 60*60*1000))
                .signWith(secretkey)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretkey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return ((extractEmail(token).equals(userDetails.getUsername())) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretkey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration().before(new Date());
    }
}
