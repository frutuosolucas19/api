package br.udesc.model;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Status extends PanacheEntity {
   
    private String nome;

    public Status() {
    }

    public Status(String nome) {
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Status{" +
                ", nome='" + nome + '\'' +
                '}';
    }
}
