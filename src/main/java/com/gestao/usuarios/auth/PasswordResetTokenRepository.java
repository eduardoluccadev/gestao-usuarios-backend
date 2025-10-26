package com.gestao.usuarios.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query("delete from PasswordResetToken t where t.user.id = :userId")
    void deleteAllByUserId(Long userId);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiresAt < :now or t.usedAt is not null")
    int deleteExpiredOrUsed(Instant now);
}
