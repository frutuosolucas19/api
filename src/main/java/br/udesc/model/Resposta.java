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

    public Resposta(Pergunta pergunta, String resposta) {
        this.resposta = resposta;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
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
                ", pergunta='" + pergunta + '\'' +
                ", resposta='" + resposta + 
                '}';
    }
}
