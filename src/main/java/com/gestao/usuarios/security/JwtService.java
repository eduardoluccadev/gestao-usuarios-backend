package com.gestao.usuarios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey accessKey;
    private final long accessTtlSeconds;
    private final String issuer;

    public JwtService(
            @Value("${jwt.issuer:gestao-usuarios}") String issuer,
            @Value("${jwt.access-ttl-seconds:3600}") long accessTtlSeconds,
            @Value("${jwt.access-secret:change-me-access-256bits-change-me!!!}") String accessSecret
    ) {
        this.issuer = issuer;
        this.accessTtlSeconds = accessTtlSeconds;
        this.accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generate(String subject, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .subject(subject)
                .claims(extraClaims == null ? Map.of() : extraClaims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .signWith(accessKey, Jwts.SIG.HS256)
                .compact();
    }

    public long getExpSeconds() {
        return accessTtlSeconds;
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(parse(token).getPayload());
    }

    public Boolean extractBooleanClaim(String token, String claimName) {
        Claims c = parse(token).getPayload();
        Object v = c.get(claimName);
        return (v instanceof Boolean) ? (Boolean) v : Boolean.FALSE;
    }


    private Jws<Claims> parse(String token) {
        // JJWT 0.12.x API
        return Jwts.parser()
                .requireIssuer(issuer)
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token);
    }
}
