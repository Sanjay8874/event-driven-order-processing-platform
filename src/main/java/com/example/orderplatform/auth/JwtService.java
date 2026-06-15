package com.example.orderplatform.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private final String issuer;
    private final SecretKey key;
    private final long accessMinutes;
    private final long refreshDays;

    public JwtService(@Value("${app.jwt.issuer}") String issuer,
                      @Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.access-token-minutes}") long accessMinutes,
                      @Value("${app.jwt.refresh-token-days}") long refreshDays) {
        this.issuer = issuer;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessMinutes = accessMinutes;
        this.refreshDays = refreshDays;
    }

    public String accessToken(UserEntity user) {
        return token(user, Instant.now().plusSeconds(accessMinutes * 60), "access");
    }

    public String refreshToken(UserEntity user) {
        return token(user, Instant.now().plusSeconds(refreshDays * 86400), "refresh");
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).requireIssuer(issuer).build().parseSignedClaims(token).getPayload();
    }

    @SuppressWarnings("unchecked")
    public List<String> roles(Claims claims) {
        return (List<String>) claims.get("roles", Collection.class);
    }

    private String token(UserEntity user, Instant expiresAt, String type) {
        List<String> roles = user.getRoles().stream().map(RoleEntity::getName).sorted().toList();
        return Jwts.builder()
                .issuer(issuer)
                .subject(user.getEmail())
                .claim("uid", user.getId().toString())
                .claim("roles", roles)
                .claim("typ", type)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }
}
