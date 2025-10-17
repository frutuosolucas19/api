package br.udesc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

@Entity
@Table(name = "usuario",
       uniqueConstraints = @UniqueConstraint(name = "ux_usuario_email", columnNames = "email"))
public class Usuario extends PanacheEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    @Column(nullable = false)
    private String email;            

    @Column(nullable = false, name = "senha_hash")
    private String senhaHash;        

    @Column(nullable = false, name = "tipo_usuario")
    private String tipoUsuario;

    @CreationTimestamp
    @Column(name = "criado_em")
    private Instant criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private Instant atualizadoEm;

    @Transient
    private String senha;

    public Usuario() {}

    public Usuario(Pessoa pessoa, String email, String senhaHash, String tipoUsuario) {
        this.pessoa = pessoa;
        this.email = email;
        this.senhaHash = senhaHash;
        this.tipoUsuario = tipoUsuario;
    }

    @PrePersist @PreUpdate
    private void normalize() {
        if (email != null) email = email.trim().toLowerCase();
        if (tipoUsuario != null) tipoUsuario = tipoUsuario.trim();
    }

    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public Instant getCriadoEm() { return criadoEm; }
    public Instant getAtualizadoEm() { return atualizadoEm; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    @Override
    public String toString() {
        return "Usuario{" +
                "pessoa=" + pessoa +
                ", email='" + email + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
}
