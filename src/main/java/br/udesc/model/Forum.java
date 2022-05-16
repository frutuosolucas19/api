package br.udesc.model;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Forum extends PanacheEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuario;

    @Access(AccessType.PROPERTY)
    @OneToMany(targetEntity = Pergunta.class)
    private List<Pergunta> perguntas;

    public Forum() {
    }

    public Forum(Usuario usuario, List<Pergunta> perguntas) {
        this.usuario = usuario;
        this.perguntas = perguntas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @ElementCollection
    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    @Override
    public String toString() {
        return "Forum{" +
                ", usuario=" + usuario +
                ", perguntas=" + perguntas +
                '}';
    }
}
