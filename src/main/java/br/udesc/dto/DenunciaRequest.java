package br.udesc.dto;

import br.udesc.model.Historico;
import br.udesc.model.Local;
import br.udesc.model.Status;

public class DenunciaRequest {
    
    private Local local;
    private String problema;
    private String sugestao;
    private String imagem;
    private String emailUsuario; 
    private Status statusAtual;
    private Historico historico;

    public DenunciaRequest() {}

    public DenunciaRequest(Local local, String problema, String sugestao, String imagem, String emailUsuario, Status statusAtual, Historico historico) {
        this.local = local;
        this.problema = problema;
        this.sugestao = sugestao;
        this.imagem = imagem;
        this.emailUsuario = emailUsuario;
        this.statusAtual = statusAtual;
        this.historico = historico;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public String getSugestao() {
        return sugestao;
    }

    public void setSugestao(String sugestao) {
        this.sugestao = sugestao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Status getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(Status statusAtual) {
        this.statusAtual = statusAtual;
    }

    public Historico getHistorico() {
        return historico;
    }

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }
}
