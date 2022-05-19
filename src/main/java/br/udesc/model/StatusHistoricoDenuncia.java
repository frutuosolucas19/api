package br.udesc.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class StatusHistoricoDenuncia extends PanacheEntity{
    
    @OneToOne
    @JoinColumn(name="status_id")
    private Status status;
    private Date horario;
    private Date data;

    @ManyToOne
    @JoinColumn(name="historico_id")
    private Historico historico;

    public StatusHistoricoDenuncia() {
    }

    public StatusHistoricoDenuncia(Status status, Date horario, Date data) { 
        this.status = status;
        this.horario = horario;
        this.data = data;
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
                ", status=" + status +
                ", horario=" + horario +
                ", data=" + data +
                '}';
    }
}
