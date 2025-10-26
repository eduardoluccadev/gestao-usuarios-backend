package com.gestao.usuarios.auth;

import com.gestao.usuarios.user.User;
import com.gestao.usuarios.user.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class ForgotPasswordService {

    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordService.class);
    private static final SecureRandom RNG = new SecureRandom();
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    private final UserRepository users;
    private final PasswordResetTokenRepository tokens;
    private final PasswordEncoder encoder;

    public ForgotPasswordService(UserRepository users,
                                 PasswordResetTokenRepository tokens,
                                 PasswordEncoder encoder) {
        this.users = users;
        this.tokens = tokens;
        this.encoder = encoder;
    }

    @Transactional
    public void startResetFlow(String email) {
        Optional<User> maybeUser = users.findByEmailIgnoreCase(email);
        if (maybeUser.isEmpty()) {
            log.info("Password reset solicitado para email inexistente: {}", email);
            return;
        }
        User user = maybeUser.get();

        tokens.deleteAllByUserId(user.getId());

        String token = generateToken();
        Instant expiresAt = Instant.now().plus(DEFAULT_TTL);

        PasswordResetToken prt = PasswordResetToken.create(user, token, expiresAt);
        tokens.save(prt);

        // Em vez de e-mail, log para teste local:
        log.info("Link de reset para {}: http://localhost:4200/reset-password?token={}", email, token);
    }

    @Transactional
    public void finishReset(String token, String newPassword) {
        PasswordResetToken prt = tokens.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (prt.isExpired()) {
            throw new IllegalArgumentException("Token expirado");
        }
        if (prt.getUsedAt() != null) {
            throw new IllegalArgumentException("Token já utilizado");
        }

        User user = prt.getUser();
        user.setPasswordHash(encoder.encode(newPassword));
        prt.markUsed();
    }

    static String generateToken() {
        byte[] bytes = new byte[32];
        RNG.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
