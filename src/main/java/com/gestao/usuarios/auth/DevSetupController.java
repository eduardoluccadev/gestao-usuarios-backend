package com.gestao.usuarios.auth;

import com.gestao.usuarios.user.User;
import com.gestao.usuarios.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class DevSetupController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public DevSetupController(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @PostMapping("/dev-seed")
    public ResponseEntity<?> seed() {
        final String email = "admin@dev.local";
        final String rawPass = "123456";

        User u = repo.findByEmailIgnoreCase(email).orElseGet(User::new);

        if (u.getId() == null) {
            u.setFullName("Admin Dev");
            u.setEmail(email);
        }
        u.setPasswordHash(encoder.encode(rawPass));

        if (u.getRoles() == null || u.getRoles().isEmpty()) {
            u.setRoles(List.of("ADMIN", "USER"));
        }
        repo.save(u);

        return ResponseEntity.ok(Map.of(
                "seeded", true,
                "email", u.getEmail(),
                "password", rawPass,
                "roles", u.getRoles()
        ));
    }

    @GetMapping("/dev-users")
    public ResponseEntity<?> users() {
        var list = repo.findAll().stream()
                .map(u -> Map.of(
                        "id", u.getId(),
                        "email", u.getEmail(),
                        "name", u.getFullName(),
                        "roles", u.getRoles()
                ))
                .toList();
        return ResponseEntity.ok(list);
    }
}
