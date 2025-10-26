package com.gestao.usuarios.user.dto;

import jakarta.validation.constraints.Email;
import java.util.List;

public record UserUpdateRequest(
        String fullName,
        @Email String email,
        List<String> roles,
        Boolean isActive,
        String password
) {}
