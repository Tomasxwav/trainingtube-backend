package com.traini.traini_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private int EXPIRATION;

    @Value("${jwt.refresh-token.expiration}")
    private int REFRESH_TOKEN_EXPIRATION;

    public String generateToken(Authentication authentication) {
        UserDetails mainUser = (UserDetails) authentication.getPrincipal();
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        
        Collection<? extends GrantedAuthority> authorities = mainUser.getAuthorities();


        return Jwts.builder()
            .setSubject(mainUser.getUsername())
            .claim("authorities", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(key)
            .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
    UserDetails mainUser = (UserDetails) authentication.getPrincipal();
    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    
    return Jwts.builder()
        .setSubject(mainUser.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
        .signWith(key)
        .compact();
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
            .setSigningKey(key).build()
            .parseClaimsJws(token)
            .getBody(); 
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean hasAuthorities(String token) {
    try {
        Claims claims = extractAllClaims(token);
        return claims.get("authorities") != null;
    } catch (Exception e) {
        return false;
    }
}


} 
