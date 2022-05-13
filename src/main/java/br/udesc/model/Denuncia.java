package br.udesc.model;

import java.awt.Image;

public class Denuncia {

    private int id;
    private Local local;
    private String problema;
    private String sugestao;
    private Usuario usuario;
    private Image foto;
    private Status statusAtual;
    private Historico historico;

    public Denuncia() {
    }

    public Denuncia(int id, Local local, String problema, String sugestao, Usuario usuario, Image foto, Status statusAtual, Historico historico) {
        this.id = id;
        this.local = local;
        this.problema = problema;
        this.sugestao = sugestao;
        this.usuario = usuario;
        this.foto = foto;
        this.statusAtual = statusAtual;
        this.historico = historico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
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

    @Override
    public String toString() {
        return "Denuncia{" +
                "id=" + id +
                ", local=" + local +
                ", problema='" + problema + '\'' +
                ", sugestao='" + sugestao + '\'' +
                ", usuario=" + usuario +
                ", foto=" + foto +
                ", statusAtual=" + statusAtual +
                ", historico=" + historico +
                '}';
    }
}
