package br.udesc.model;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
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

    public Pergunta(Forum forum, String pergunta, List<Resposta> respostas) {
        this.forum=forum;
        this.pergunta = pergunta;
        this.respostas = respostas;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
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
                ", forum='" + forum + '\'' +
                ", pergunta='" + pergunta + '\'' +
                ", respostas=" + respostas +
                '}';
    }
}
