package br.udesc.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Usuario extends PanacheEntity{

    @OneToOne(cascade = CascadeType.ALL)
    private Pessoa pessoa;
    private String tipoUsuario;
    private String login;
    private String senha;

    public Usuario() {
    }

    public Usuario(Pessoa pessoa, String tipoUsuario, String login, String senha) {
        this.pessoa = pessoa;
        this.tipoUsuario = tipoUsuario;
        this.login = login;
        this.senha = senha;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "pessoa=" + pessoa +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
