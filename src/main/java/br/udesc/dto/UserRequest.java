package br.udesc.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRequest {
    @NotBlank(message = "Email obrigatorio.")
    @Email(message = "Email invalido.")
    public String email;

    @NotBlank(message = "Senha obrigatoria.")
    @Size(min = 8, message = "Senha deve ter no minimo 8 caracteres.")
    public String senha;

    @NotBlank(message = "Tipo de usuario obrigatorio.")
    public String tipoUsuario;

    @NotNull(message = "Pessoa obrigatoria.")
    @Valid
    public PersonRequest pessoa;
}
