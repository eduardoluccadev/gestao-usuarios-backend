package com.gestao.usuarios.config;

import com.gestao.usuarios.user.User;
import com.gestao.usuarios.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DevAdminSeed implements CommandLineRunner {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public DevAdminSeed(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        final String email = "admin@empresa.com";
        final String raw = "123456";

        var opt = repo.findByEmailIgnoreCase(email);

        if (opt.isEmpty()) {
            User admin = User.builder()
                    .fullName("Administrador")
                    .email(email)
                    .isActive(true)
                    .roles(List.of("ADMIN"))
                    .passwordHash(encoder.encode(raw)) // <- BCRYPT!
                    .build();
            repo.save(admin);
            System.out.println("[DEV] ADMIN criado: " + email + " / senha: " + raw);
            return;
        }

        var u = opt.get();
        String hash = u.getPasswordHash();
        if (hash == null || !hash.startsWith("$2")) {
            u.setPasswordHash(encoder.encode(raw));
            repo.save(u);
            System.out.println("[DEV] ADMIN atualizado com senha BCRYPT.");
        }
    }
}
