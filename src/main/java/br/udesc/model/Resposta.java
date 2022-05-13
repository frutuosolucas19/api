package br.udesc.model;

public class Resposta {
    
    private int id;
    private String resposta;

    public Resposta() {
    }

    public Resposta(int id, String resposta) {
        this.id = id;
        this.resposta = resposta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", resposta='" + resposta + '\'' +
                '}';
    }
}
