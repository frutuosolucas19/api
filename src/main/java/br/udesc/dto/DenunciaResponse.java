package br.udesc.dto;

import java.time.Instant;
import java.util.List;

public class DenunciaResponse {
    public Long id;
    public String nomeLocal;
    public EnderecoResponse endereco;
    public String problema;
    public String sugestao;
    public String emailUsuario;
    public Instant criadoEm;
    public String status;
    public List<ImagemMeta> imagens;

    public static class EnderecoResponse {
        public String logradouro;
        public Integer numero;
        public String bairro;
        public String cidade;
        public String uf;
        public String cep;
        public String complemento;
    }

    public static class ImagemMeta {
        public Long id;
        public String filename;
        public String contentType;
        public Integer ordem;
        public Long tamanhoBytes;
        public String url;
    }

    public DenunciaResponse(Long id, String nomeLocal, EnderecoResponse endereco,
                            String problema, String sugestao, String emailUsuario,
                            Instant criadoEm, String status, List<ImagemMeta> imagens) {
        this.id = id;
        this.nomeLocal = nomeLocal;
        this.endereco = endereco;
        this.problema = problema;
        this.sugestao = sugestao;
        this.emailUsuario = emailUsuario;
        this.criadoEm = criadoEm;
        this.status = status;
        this.imagens = imagens;
    }
}
