package br.udesc.dto;

import java.util.List;

public class ReportRequest {
    public String nomeLocal;
    public EnderecoRequest endereco;
    public String problema;
    public String sugestao;
    public List<ImagemRequest> imagens;

    public static class EnderecoRequest {
        public String logradouro;
        public Integer numero;
        public String bairro;
        public String cidade;
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

