package br.udesc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "denuncia")
public class Denuncia extends PanacheEntity {

    @Column(name = "nome_local", nullable = false, length = 200)
    private String nomeLocal;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    @Column(nullable = false, columnDefinition = "text")
    private String problema;

    @Column(columnDefinition = "text")
    private String sugestao;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private Instant criadoEm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "status_id")
    private Status statusAtual;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "historico_id")
    private Historico historico;

    @OneToMany(
            mappedBy = "denuncia",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("ordem ASC, id ASC")
    private List<Imagem> imagens = new ArrayList<>();

    public Denuncia() {}

    public Denuncia(String nomeLocal, Endereco endereco, String problema, String sugestao) {
        this.nomeLocal = nomeLocal;
        this.endereco = endereco;
        this.problema = problema;
        this.sugestao = sugestao;
    }

    public void addImagem(Imagem img) {
        if (img == null) return;
        img.setDenuncia(this);
        this.imagens.add(img);
    }

    public void removeImagem(Imagem img) {
        if (img == null) return;
        img.setDenuncia(null);
        this.imagens.remove(img);
    }

    public String getNomeLocal() { return nomeLocal; }
    public void setNomeLocal(String nomeLocal) { this.nomeLocal = nomeLocal; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public String getProblema() { return problema; }
    public void setProblema(String problema) { this.problema = problema; }

    public String getSugestao() { return sugestao; }
    public void setSugestao(String sugestao) { this.sugestao = sugestao; }

    public Instant getCriadoEm() { return criadoEm; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Status getStatusAtual() { return statusAtual; }
    public void setStatusAtual(Status statusAtual) { this.statusAtual = statusAtual; }

    public Historico getHistorico() { return historico; }
    public void setHistorico(Historico historico) { this.historico = historico; }

    public List<Imagem> getImagens() { return imagens; }
    public void setImagens(List<Imagem> imagens) { this.imagens = imagens; }
}
