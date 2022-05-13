package br.udesc.model;

import java.util.List;

public class Historico {
    
    private int id;
    private List<StatusHistoricoDenuncia> status;

    public Historico() {
    }

    public Historico(int id, List<StatusHistoricoDenuncia> status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<StatusHistoricoDenuncia> getStatus() {
        return status;
    }

    public void setStatus(List<StatusHistoricoDenuncia> status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Historico{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
