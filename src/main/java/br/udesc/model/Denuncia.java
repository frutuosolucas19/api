package br.udesc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Denuncia extends PanacheEntity{

    @OneToOne
    @JoinColumn(name="local_id")
    private Local local;

    private String problema;
    private String sugestao;


    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
    
    private String imagem;

    @OneToOne
    @JoinColumn(name="status_id")
    private Status statusAtual;

    @OneToOne
    @JoinColumn(name="historico_id")
    private Historico historico;

    public Denuncia() {
    }

    public Denuncia(Local local, String problema, String sugestao, Usuario usuario, String imagem, Status statusAtual, Historico historico) {
        this.local = local;
        this.problema = problema;
        this.sugestao = sugestao;
        this.usuario = usuario;
        this.imagem = imagem;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getImagem() {
    return this.imagem;
    }
    
    public void setImagem(String imagem) { 
    this.imagem = imagem;
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
                ", local=" + local +
                ", problema='" + problema + '\'' +
                ", sugestao='" + sugestao + '\'' +
                ", usuario=" + usuario +
                ", imagem=" + imagem +
                ", statusAtual=" + statusAtual +
                ", historico=" + historico +
                '}';
    }
}
