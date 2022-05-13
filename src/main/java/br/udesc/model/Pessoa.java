package br.udesc.model;

import java.awt.Image;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Pessoa extends PanacheEntity{

    private String nome;
    private String usuario;
    private String email;
    //private Image imagem;

    public Pessoa() {
    }

    public Pessoa(String nome, String usuario, String email
    //,Image imagem
    ){
        this.nome = nome;
        this.usuario = usuario;
        this.email = email;
       // this.imagem = imagem;
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

    /* 
    public Image getImagem() {
        return imagem;
    }

    public void setImagem(Image imagem) {
        this.imagem = imagem;
    }
    */

    @Override
    public String toString() {
        return "Pessoa{" +
                //"id=" + id +
                ", nome='" + nome + '\'' +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
               // ", imagem=" + imagem +
                '}';
    }
}
