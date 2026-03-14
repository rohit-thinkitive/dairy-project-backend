package com.dairy.milkcollection.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long adminExpirationMs;
    private final long farmerExpirationMs;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.admin-expiration-ms}") long adminExpirationMs,
            @Value("${app.jwt.farmer-expiration-ms}") long farmerExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.adminExpirationMs = adminExpirationMs;
        this.farmerExpirationMs = farmerExpirationMs;
    }

    public String generateAdminToken(String username, String role) {
        return generateToken(username, role, adminExpirationMs);
    }

    public String generateFarmerToken(String farmerId, String mobile) {
        return Jwts.builder()
                .subject(farmerId)
                .claim("role", "FARMER")
                .claim("mobile", mobile)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + farmerExpirationMs))
                .signWith(key)
                .compact();
    }

    private String generateToken(String subject, String role, long expirationMs) {
        return Jwts.builder()
                .subject(subject)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        return getClaims(token).getPayload().getSubject();
    }

    public String getRoleFromToken(String token) {
        return getClaims(token).getPayload().get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public long getAdminExpirationMs() {
        return adminExpirationMs;
    }

    public long getFarmerExpirationMs() {
        return farmerExpirationMs;
    }
}
