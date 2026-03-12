package br.udesc.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "local")
public class Place extends PanacheEntity{
    
    private String nome;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="localizacao_id")
    private Location localizacao;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="endereco_id")
    private Address endereco;

    public Place() {
    }

    public Place(String nome, Location localizacao, Address endereco) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Location getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Location localizacao) {
        this.localizacao = localizacao;
    }

    public Address getEndereco() {
        return endereco;
    }

    public void setEndereco(Address endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", localizacao=" + localizacao +
                ", endereco=" + endereco +
                '}';
    }
}


