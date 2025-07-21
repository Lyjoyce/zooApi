package com.zoo.api.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.zoo.api.entities.Account;
import com.zoo.api.entities.Adult;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24h

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ---- Génération du token pour Adult avec firstName
    public String generateToken(Adult adult) {
        return Jwts.builder()
                .setSubject(adult.getEmail())
                .claim("id", adult.getId())
                .claim("firstName", adult.getFirstName()) // 👈 ajouté ici
                .claim("adultType", adult.getType() != null ? adult.getType().name() : "UNKNOWN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ---- Génération du token pour Account
    public String generateToken(Account account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", account.getRole().name());
        return createToken(claims, account.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractFirstName(String token) {
        Claims claims = parseClaims(token);
        return claims.get("firstName", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = parseClaims(token);
            String username = claims.getSubject();
            return (username.equals(userDetails.getUsername()) &&
                    !claims.getExpiration().before(new Date()));
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
