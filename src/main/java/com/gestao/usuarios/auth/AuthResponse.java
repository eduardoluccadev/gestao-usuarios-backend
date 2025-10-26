package com.gestao.usuarios.auth;

import java.util.List;

public record AuthResponse(
        String token,
        long expiresIn,
        String name,
        List<String>  roles
) {}
