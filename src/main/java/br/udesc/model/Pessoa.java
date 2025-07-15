package br.udesc.model;

import javax.persistence.Entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Pessoa extends PanacheEntity {

    private String nome;
    private String imagem;

    public Pessoa() {
    }

    public Pessoa(String nome, String imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "nome='" + nome + '\'' +
                ", imagem='" + imagem + '\'' +
                '}';
    }
}
