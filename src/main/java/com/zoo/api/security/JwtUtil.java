package com.zoo.api.security;

import com.zoo.api.entities.Adult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")// injection depuis application.properties
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24h
    private Key key;

    @PostConstruct
    public void init() {
    	this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes()); 

    }

    public String generateToken(Adult adult) {
        return Jwts.builder()
                .setSubject(adult.getEmail())
                .claim("id", adult.getId())
                .claim("role", adult.getRole().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Nouvelle méthode qui valide le token ET correspondance avec userDetails
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = parseClaims(token);
            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            return (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
