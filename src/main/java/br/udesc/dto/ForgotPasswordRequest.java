package br.udesc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {
    @NotBlank(message = "Email obrigatorio.")
    @Email(message = "Email invalido.")
    public String email;
}
