package com.destleiloes.adapter.in.web.security;

import com.destleiloes.domain.model.Role;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Emite e valida o mesmo formato de JWT que o middleware do Next.js
 * (src/lib/session.ts) espera: HS256, chave = bytes UTF-8 crus de
 * SESSION_SECRET (sem base64-decode), claims userId/role/expiresAt.
 */
@Service
public class JwtService {

    private static final long SESSION_DURATION_MS = 7L * 24 * 60 * 60 * 1000;

    private final SecretKey key;

    public JwtService(@Value("${app.session-secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userId, Role role) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(SESSION_DURATION_MS);

        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role.name())
                .claim("expiresAt", expiresAt.toEpochMilli())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Optional<SessionClaims> parseToken(String token) {
        try {
            var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            String userId = claims.get("userId", String.class);
            String roleStr = claims.get("role", String.class);
            if (userId == null || roleStr == null) {
                return Optional.empty();
            }
            return Optional.of(new SessionClaims(userId, Role.valueOf(roleStr)));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public long sessionDurationMs() {
        return SESSION_DURATION_MS;
    }

    public record SessionClaims(String userId, Role role) {}
}
