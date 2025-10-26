package com.gestao.usuarios.user;

import com.gestao.usuarios.user.dto.UserCreateRequest;
import com.gestao.usuarios.user.dto.UserDTO;
import com.gestao.usuarios.user.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    @GetMapping
    public Page<UserDTO> list(Pageable pageable, @RequestParam(name = "search", required = false) String search) {
        return service.list(search, pageable);
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable Long id) {
        return service.findByIdDTO(id);
    }

    @PostMapping
    public UserDTO create(@RequestBody UserCreateRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UserUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
