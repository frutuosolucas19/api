package br.udesc.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ReportRequest {
    @NotBlank(message = "nomeLocal obrigatorio.")
    public String nomeLocal;

    @NotNull(message = "endereco obrigatorio.")
    @Valid
    public EnderecoRequest endereco;

    @NotBlank(message = "problema obrigatorio.")
    public String problema;

    public String sugestao;
    public List<ImagemRequest> imagens;

    public static class EnderecoRequest {
        public String logradouro;
        public Integer numero;
        public String bairro;

        @NotBlank(message = "endereco.cidade obrigatorio.")
        public String cidade;

        @NotBlank(message = "endereco.uf obrigatorio.")
        public String uf;

        public String cep;
        public String complemento;
    }

    public static class ImagemRequest {
        public String base64;
        public String contentType;
        public String filename;
        public Integer ordem;
    }
}
