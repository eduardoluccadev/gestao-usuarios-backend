package com.gestao.usuarios.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserCreateRequest(
        @NotBlank String fullName,
        @Email String email,
        List<String> roles,
        Boolean isActive,
        @NotBlank String password
) {}
