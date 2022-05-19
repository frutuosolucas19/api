package br.udesc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Pergunta extends PanacheEntity {
    
    private String pergunta;

    @ManyToOne
    @JoinColumn(name="forum_id")
    private Forum forum;
   
    public Pergunta() {
    }

    public Pergunta(Forum forum, String pergunta) {
        this.forum=forum;
        this.pergunta = pergunta;
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
    
    @Override
    public String toString() {
        return "Pergunta{" +
                ", forum='" + forum + '\'' +
                ", pergunta='" + pergunta + 
                '}';
    }
}
