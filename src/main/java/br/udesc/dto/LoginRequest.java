package br.udesc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Email obrigatorio.")
    @Email(message = "Email invalido.")
    public String email;

    @NotBlank(message = "Senha obrigatoria.")
    public String senha;
}
