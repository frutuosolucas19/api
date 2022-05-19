package br.udesc.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Pergunta extends PanacheEntity {
    
    private String pergunta;

    @ManyToOne
    @JoinColumn(name="forum_id")
    private Forum forum;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="pergunta_id")
    private List<Resposta> respostas = new ArrayList<>();
   
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
