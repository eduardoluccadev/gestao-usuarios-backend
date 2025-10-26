package com.gestao.usuarios.auth;

import java.util.List;

public record MeResponse(
        String email,
        String fullName,
        List<String> roles
) {}
