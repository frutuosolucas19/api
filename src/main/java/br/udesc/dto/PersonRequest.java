package br.udesc.dto;

import jakarta.validation.constraints.NotBlank;

public class PersonRequest {
    @NotBlank(message = "Nome obrigatorio.")
    public String nome;
    public String imagem;
}
