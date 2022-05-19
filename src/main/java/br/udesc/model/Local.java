package br.udesc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Local extends PanacheEntity{
    
    private String nome;

    @OneToOne
    @JoinColumn(name="localizacao_id")
    private Localizacao localizacao;
    
    @OneToOne
    @JoinColumn(name="endereco_id")
    private Endereco endereco;

    public Local() {
    }

    public Local(String nome, Localizacao localizacao, Endereco endereco) {
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

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Local{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", localizacao=" + localizacao +
                ", endereco=" + endereco +
                '}';
    }
}
