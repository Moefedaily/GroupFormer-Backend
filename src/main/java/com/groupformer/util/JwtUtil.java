package com.groupformer.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    @Value("${jwt.verification.expiration:3600000}")
    private Long verificationExpiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public Boolean extractEmailVerified(String token) {
        Boolean verified = extractAllClaims(token).get("emailVerified", Boolean.class);
        return verified != null ? verified : false;
    }

    public String extractTokenPurpose(String token) {
        return extractAllClaims(token).get("purpose", String.class);
    }

    public String generateVerifiedToken(String username, String role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        claims.put("emailVerified", true);
        claims.put("purpose", "LOGIN");

        return createToken(claims, username, expiration);
    }

    public String generateUnverifiedToken(String username, String role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        claims.put("emailVerified", false);
        claims.put("purpose", "UNVERIFIED_USER");

        return createToken(claims, username, expiration);
    }

    public String generateEmailVerificationToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("purpose", "EMAIL_VERIFICATION");
        claims.put("verificationId", UUID.randomUUID().toString());

        return createToken(claims, username, verificationExpiration);
    }

    public String generatePasswordResetToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("purpose", "PASSWORD_RESET");
        claims.put("resetId", UUID.randomUUID().toString());

        return createToken(claims, username, verificationExpiration);
    }

    private String createToken(Map<String, Object> claims, String subject, Long tokenExpiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean validateEmailVerificationToken(String token) {
        try {
            String purpose = extractTokenPurpose(token);
            return "EMAIL_VERIFICATION".equals(purpose) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean validatePasswordResetToken(String token) {
        try {
            String purpose = extractTokenPurpose(token);
            return "PASSWORD_RESET".equals(purpose) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}