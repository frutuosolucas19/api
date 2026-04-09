package br.udesc.dto;

import jakarta.validation.constraints.NotNull;

public class ForumRequest {
    @NotNull(message = "usuarioId obrigatorio.")
    public Long usuarioId;
}
