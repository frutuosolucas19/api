package br.udesc.model;

import java.util.List;

public class Pergunta {
    
    private int id;
    private String pergunta;
    private List<Resposta> respostas;

    public Pergunta() {
    }

    public Pergunta(int id, String pergunta, List<Resposta> respostas) {
        this.id = id;
        this.pergunta = pergunta;
        this.respostas = respostas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    @Override
    public String toString() {
        return "Pergunta{" +
                "id=" + id +
                ", pergunta='" + pergunta + '\'' +
                ", respostas=" + respostas +
                '}';
    }
}
