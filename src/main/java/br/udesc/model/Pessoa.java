package br.udesc.model;

import java.awt.Image;

import javax.persistence.Column;
import javax.persistence.Entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Pessoa extends PanacheEntity{

    private String nome;
    private String usuario;
    private String email;
    private byte[] imagem;

    public Pessoa() {
    }

    public Pessoa(String nome, String usuario, String email, byte[] imagem){
        this.nome = nome;
        this.usuario = usuario;
        this.email = email;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //@Column(name= "imagem")
    public byte[] getImagem() {
    return this.imagem;
    }

    public void setImagem(byte[] imagem) { 
    this.imagem = imagem;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                ", nome='" + nome + '\'' +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", imagem=" + imagem +
                '}';
    }
}
