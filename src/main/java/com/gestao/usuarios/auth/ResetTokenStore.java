package com.gestao.usuarios.auth;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResetTokenStore {

    private static final long EXPIRATION_SECONDS = 15 * 60; // 15 minutos

    private static final class Entry {
        final String email;
        final Instant expiresAt;
        Entry(String email, Instant expiresAt) {
            this.email = email;
            this.expiresAt = expiresAt;
        }
    }

    private final Map<String, Entry> tokens = new ConcurrentHashMap<>();

    public String issueToken(String email) {
        var token = UUID.randomUUID().toString().replace("-", "");
        var exp = Instant.now().plusSeconds(EXPIRATION_SECONDS);
        tokens.put(token, new Entry(email, exp));
        return token;
    }

    public Optional<String> consumeIfValid(String token) {
        var entry = tokens.get(token);
        if (entry == null) return Optional.empty();
        if (Instant.now().isAfter(entry.expiresAt)) {
            tokens.remove(token);
            return Optional.empty();
        }
        tokens.remove(token);
        return Optional.of(entry.email);
    }

    public Optional<String> peekEmail(String token) {
        var entry = tokens.get(token);
        if (entry == null || Instant.now().isAfter(entry.expiresAt)) return Optional.empty();
        return Optional.of(entry.email);
    }
}
