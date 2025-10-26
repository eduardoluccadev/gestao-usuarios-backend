package com.gestao.usuarios.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres")
    private String password;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
