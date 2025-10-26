package com.gestao.usuarios.user;

import com.gestao.usuarios.user.dto.UserCreateRequest;
import com.gestao.usuarios.user.dto.UserDTO;
import com.gestao.usuarios.user.dto.UserUpdateRequest;
import com.gestao.usuarios.user.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public Page<UserDTO> list(String q, Pageable pageable) {
        if (q != null && !q.isBlank()) {
            var page = repo.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, pageable);
            return page.map(UserMapper::toDTO);
        }
        return repo.findAll(pageable).map(UserMapper::toDTO);
    }

    @Transactional
    public UserDTO findByIdDTO(Long id) {
        var u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return UserMapper.toDTO(u);
    }

    @Transactional
    public UserDTO create(UserCreateRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("Requisição inválida");
        }
        if (req.password() == null || req.password().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }

        String hash = encoder.encode(req.password());
        var u = UserMapper.fromCreate(req, hash);

        var saved = repo.save(u);
        return UserMapper.toDTO(saved);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateRequest req) {
        var u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        UserMapper.applyUpdate(u, req);
        var saved = repo.save(u);

        return UserMapper.toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
