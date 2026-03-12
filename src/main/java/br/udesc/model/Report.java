package br.udesc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "denuncia")
public class Report extends PanacheEntity {

    @Column(name = "nome_local", nullable = false, length = 200)
    private String nomeLocal;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Address endereco;

    @Column(nullable = false, columnDefinition = "text")
    private String problema;

    @Column(columnDefinition = "text")
    private String sugestao;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private Instant criadoEm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC, id ASC")
    private List<Image> imagens = new ArrayList<>();

    public Report() {}

    @PrePersist
    private void prePersist() {
        if (status == null) status = Status.ENCAMINHADO;
    }

    public void addImagem(Image img) {
        if (img == null) return;
        img.setDenuncia(this);
        this.imagens.add(img);
    }
    public void removeImagem(Image img) {
        if (img == null) return;
        img.setDenuncia(null);
        this.imagens.remove(img);
    }

    public String getNomeLocal() { return nomeLocal; }
    public void setNomeLocal(String nomeLocal) { this.nomeLocal = nomeLocal; }

    public Address getEndereco() { return endereco; }
    public void setEndereco(Address endereco) { this.endereco = endereco; }

    public String getProblema() { return problema; }
    public void setProblema(String problema) { this.problema = problema; }

    public String getSugestao() { return sugestao; }
    public void setSugestao(String sugestao) { this.sugestao = sugestao; }

    public Instant getCriadoEm() { return criadoEm; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Image> getImagens() { return imagens; }
    public void setImagens(List<Image> imagens) { this.imagens = imagens; }
}


