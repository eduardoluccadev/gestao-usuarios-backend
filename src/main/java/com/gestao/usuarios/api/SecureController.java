package com.gestao.usuarios.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SecureController {

    @GetMapping("/secure/ping")
    public ResponseEntity<String> securePing() {
        return ResponseEntity.ok("ok");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/ping")
    public ResponseEntity<String> adminPing() {
        return ResponseEntity.ok("admin-ok");
    }
}
