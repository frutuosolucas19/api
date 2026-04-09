package br.udesc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnswerRequest {
    @NotBlank(message = "resposta obrigatoria.")
    public String resposta;

    @NotNull(message = "perguntaId obrigatorio.")
    public Long perguntaId;
}
