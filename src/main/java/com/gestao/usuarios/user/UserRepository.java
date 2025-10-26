package com.gestao.usuarios.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    org.springframework.data.domain.Page<User>
    findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String fullName, String email,
            org.springframework.data.domain.Pageable pageable
    );

}
