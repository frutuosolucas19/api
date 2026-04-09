package br.udesc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    @NotBlank(message = "Token obrigatorio.")
    public String tokenAws;

    @NotBlank(message = "Nova senha obrigatoria.")
    @Size(min = 8, message = "Senha deve ter no minimo 8 caracteres.")
    public String novaSenha;
}
