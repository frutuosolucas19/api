package br.udesc.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Denuncia extends PanacheEntity{

    @OneToOne(cascade = CascadeType.ALL)
    private Local local;

    private String problema;
    private String sugestao;

    @Access(AccessType.PROPERTY)
    @ManyToOne(targetEntity = Usuario.class)
    private Usuario usuario;
    
    private byte[] imagem;

    @OneToOne(cascade = CascadeType.ALL)
    private Status statusAtual;

    @OneToOne(cascade = CascadeType.ALL)
    private Historico historico;

    public Denuncia() {
    }

    public Denuncia(Local local, String problema, String sugestao, Usuario usuario, byte[] imagem, Status statusAtual, Historico historico) {
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

    public byte[] getImagem() {
    return this.imagem;
    }
    
    public void setImagem(byte[] imagem) { 
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
