package br.udesc.model;

import java.util.List;

public class Forum {

    private int id;
    private Usuario usuario;
    private List<Pergunta> perguntas;

    public Forum() {
    }

    public Forum(int id, Usuario usuario, List<Pergunta> perguntas) {
        this.id = id;
        this.usuario = usuario;
        this.perguntas = perguntas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", perguntas=" + perguntas +
                '}';
    }
}
