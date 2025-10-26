package com.gestao.usuarios.auth;

import com.gestao.usuarios.security.JwtService;
import com.gestao.usuarios.user.User;
import com.gestao.usuarios.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwt;

    public AuthController(UserRepository repo, PasswordEncoder passwordEncoder, JwtService jwt) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User u = repo.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        Map<String, Object> claims = Map.of("roles", u.getRoles());
        String token = jwt.generate(u.getEmail(), claims);

        return ResponseEntity.ok(new AuthResponse(
                token,
                jwt.getExpSeconds(),
                u.getFullName(),
                u.getRoles()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User u)) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuário não autenticado"));
        }

        return ResponseEntity.ok(Map.of(
                "email", u.getEmail(),
                "fullName", u.getFullName(),
                "roles", u.getRoles()
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        if (isBlank(req.name()) || isBlank(req.email()) || isBlank(req.password())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", "INVALID_PAYLOAD",
                    "message", "name, email e password são obrigatórios"
            ));
        }

        if (repo.findByEmailIgnoreCase(req.email()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of(
                    "code", "EMAIL_TAKEN",
                    "message", "E-mail já cadastrado"
            ));
        }

        var u = new User();
        u.setFullName(req.name());
        u.setEmail(req.email());
        u.setPasswordHash(passwordEncoder.encode(req.password()));
        u.setRoles(List.of("USER"));
        u.setActive(true);

        repo.save(u);

        return ResponseEntity.status(201).body(Map.of(
                "id", u.getId(),
                "name", u.getFullName(),
                "email", u.getEmail(),
                "roles", u.getRoles(),
                "active", u.isActive()
        ));
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody ForgotRequest req) {
        var opt = repo.findByEmailIgnoreCase(req.email());
        if (opt.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "message", "Se o e-mail existir, enviaremos instruções."
            ));
        }

        String resetToken = jwt.generate(req.email(), Map.of(
                "pwd_reset", true,
                "iat", Instant.now().getEpochSecond()
        ));

        return ResponseEntity.ok(Map.of(
                "message", "Instruções enviadas (simulação).",
                "token", resetToken
        ));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetRequest req) {
        if (!jwt.isValid(req.token())) {
            return ResponseEntity.status(400).body(Map.of(
                    "code", "INVALID_TOKEN",
                    "message", "Link inválido ou expirado."
            ));
        }

        Boolean isReset = jwt.extractBooleanClaim(req.token(), "pwd_reset");
        if (isReset == null || !isReset) {
            return ResponseEntity.status(400).body(Map.of(
                    "code", "INVALID_TOKEN",
                    "message", "Link inválido ou expirado."
            ));
        }

        String email = jwt.extractSubject(req.token());
        var user = repo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));

        user.setPasswordHash(passwordEncoder.encode(req.password()));
        repo.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Senha redefinida com sucesso."
        ));
    }

    public record LoginRequest(String email, String password) {}
    public record AuthResponse(String token, long expiresIn, String name, List<String> roles) {}
    public record SignupRequest(String name, String email, String password) {}

    public record ForgotRequest(String email) {}
    public record ResetRequest(String token, String password) {}

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}


