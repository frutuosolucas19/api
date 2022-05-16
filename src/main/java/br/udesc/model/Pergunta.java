package br.udesc.model;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Pergunta extends PanacheEntity {
    
    private String pergunta;

    @Access(AccessType.PROPERTY)
    @OneToMany(targetEntity = Resposta.class)
    private List<Resposta> respostas;

    public Pergunta() {
    }

    public Pergunta(String pergunta, List<Resposta> respostas) {
        this.pergunta = pergunta;
        this.respostas = respostas;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    @ElementCollection
    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    @Override
    public String toString() {
        return "Pergunta{" +
                ", pergunta='" + pergunta + '\'' +
                ", respostas=" + respostas +
                '}';
    }
}
