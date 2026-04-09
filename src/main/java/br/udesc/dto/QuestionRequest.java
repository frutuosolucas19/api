package br.udesc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionRequest {
    @NotBlank(message = "pergunta obrigatoria.")
    public String pergunta;

    @NotNull(message = "forumId obrigatorio.")
    public Long forumId;
}
