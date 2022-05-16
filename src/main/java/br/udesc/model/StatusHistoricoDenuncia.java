package br.udesc.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class StatusHistoricoDenuncia extends PanacheEntity{
    
    @OneToOne(cascade = CascadeType.ALL)
    private Status status;
    private Date horario;
    private Date data;

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
