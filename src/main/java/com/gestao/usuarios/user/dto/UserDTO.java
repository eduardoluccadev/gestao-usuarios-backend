package com.gestao.usuarios.user.dto;

import java.util.List;

public record UserDTO(Long id, String fullName, String email, boolean isActive, List<String> roles) {}
