package br.udesc.model;

import java.util.Date;

public class StatusHistoricoDenuncia {
    private int id;
    private Status status;
    private Date horario;
    private Date data;

    public StatusHistoricoDenuncia() {
    }

    public StatusHistoricoDenuncia(int id, Status status, Date horario, Date data) {
        this.id = id;
        this.status = status;
        this.horario = horario;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StatusHistoricoDenuncia{" +
                "id=" + id +
                ", status=" + status +
                ", horario=" + horario +
                ", data=" + data +
                '}';
    }
}
