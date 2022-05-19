package br.udesc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Resposta extends PanacheEntity {
    
    private String resposta;

    @ManyToOne
    @JoinColumn(name="pergunta_id")
    private Pergunta pergunta;

    public Resposta() {
    }

    public Resposta(String resposta) {
        this.resposta = resposta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    @Override
    public String toString() {
        return "Resposta{" +
                ", resposta='" + resposta + 
                '}';
    }
}
