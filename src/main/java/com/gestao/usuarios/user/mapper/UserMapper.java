package com.gestao.usuarios.user.mapper;

import com.gestao.usuarios.user.User;
import com.gestao.usuarios.user.dto.UserCreateRequest;
import com.gestao.usuarios.user.dto.UserDTO;
import com.gestao.usuarios.user.dto.UserUpdateRequest;

import java.util.List;

public final class UserMapper {

    private UserMapper() {}

    public static UserDTO toDTO(User u) {
        if (u == null) return null;
        return new UserDTO(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.isActive(),
                u.getRoles()
        );
    }

    public static User fromCreate(UserCreateRequest req, String passwordHash) {
        if (req == null) return null;

        List<String> roles = (req.roles() == null || req.roles().isEmpty())
                ? List.of("USER")
                : req.roles();

        boolean active = req.isActive() == null || req.isActive();

        String fullName = req.fullName() == null ? null : req.fullName().trim();
        String email    = req.email() == null ? null : req.email().trim().toLowerCase();

        return User.builder()
                .fullName(fullName)
                .email(email)
                .passwordHash(passwordHash)
                .roles(roles)
                .isActive(active)
                .build();
    }

    public static void applyUpdate(User u, UserUpdateRequest req) {
        if (u == null || req == null) return;

        if (req.fullName() != null && !req.fullName().isBlank()) {
            u.setFullName(req.fullName().trim());
        }

        if (req.email() != null && !req.email().isBlank()) {
            u.setEmail(req.email().trim().toLowerCase());
        }

        if (req.roles() != null && !req.roles().isEmpty()) {
            u.setRoles(req.roles());
        }

        if (req.isActive() != null) {
            u.setActive(req.isActive());
        }
    }
}
