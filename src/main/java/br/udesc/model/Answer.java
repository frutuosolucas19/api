package br.udesc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "resposta")
public class Answer extends PanacheEntity {
    
    private String resposta;

    @ManyToOne
    @JoinColumn(name="pergunta_id")
    private Question pergunta;

    public Answer() {
    }

    public Answer(Question pergunta, String resposta) {
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public Question getPergunta() {
        return pergunta;
    }

    public void setPergunta(Question pergunta) {
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
        return "Answer{" +
                ", pergunta='" + pergunta + '\'' +
                ", resposta='" + resposta + 
                '}';
    }
}


