package br.udesc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "pergunta")
public class Question extends PanacheEntity {
    
    private String pergunta;

    @ManyToOne
    @JoinColumn(name="forum_id")
    private Forum forum;
   
    public Question() {
    }

    public Question(Forum forum, String pergunta) {
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
        return "Question{" +
                ", forum='" + forum + '\'' +
                ", pergunta='" + pergunta + 
                '}';
    }
}


