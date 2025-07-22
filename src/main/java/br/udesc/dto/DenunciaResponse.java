package br.udesc.dto;

import br.udesc.model.Denuncia;

public class DenunciaResponse {
    public Long id;
    public String problema;
    public String status;
    public String nomeUsuario;

    public DenunciaResponse(Denuncia denuncia) {
        this.id = denuncia.id;
        this.problema = denuncia.getProblema();
        this.status = denuncia.getStatusAtual().toString();
        this.nomeUsuario = denuncia.getUsuario().getPessoa().getNome();
    }
}
