package br.udesc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Usuario extends PanacheEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    private String email;
    private String senha;
    private String tipoUsuario;

    public Usuario() {
    }

    public Usuario(Pessoa pessoa, String email, String senha, String tipoUsuario) {
        this.pessoa = pessoa;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "pessoa=" + pessoa +
                ", email='" + email + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
}
