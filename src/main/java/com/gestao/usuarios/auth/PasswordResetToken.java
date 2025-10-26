package com.gestao.usuarios.auth;

import com.gestao.usuarios.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 120, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false, columnDefinition = "datetime2")
    private Instant expiresAt;

    @Column(name = "used_at", columnDefinition = "datetime2")
    private Instant usedAt;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime2")
    private Instant createdAt;

    public static PasswordResetToken create(User user, String token, Instant expiresAt) {
        PasswordResetToken prt = new PasswordResetToken();
        prt.id = UUID.randomUUID();
        prt.user = user;
        prt.token = token;
        prt.expiresAt = expiresAt;
        prt.createdAt = Instant.now();
        return prt;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public void markUsed() {
        this.usedAt = Instant.now();
    }
}
