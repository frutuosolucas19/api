package br.udesc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "imagem")
public class Imagem extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "denuncia_id", nullable = false)
    private Denuncia denuncia;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "dados", nullable = false)
    private byte[] dados;

    @Column(name = "content_type", length = 100, nullable = false)
    private String contentType;

    @Column(name = "filename", length = 255)
    private String filename;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "ordem")
    private Integer ordem;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private Instant criadoEm;

    public Denuncia getDenuncia() { return denuncia; }
    public void setDenuncia(Denuncia denuncia) { this.denuncia = denuncia; }

    public byte[] getDados() { return dados; }
    public void setDados(byte[] dados) { this.dados = dados; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public Long getTamanhoBytes() { return tamanhoBytes; }
    public void setTamanhoBytes(Long tamanhoBytes) { this.tamanhoBytes = tamanhoBytes; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public Instant getCriadoEm() { return criadoEm; }
}

