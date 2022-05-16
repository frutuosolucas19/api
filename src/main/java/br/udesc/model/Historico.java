package br.udesc.model;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Historico extends PanacheEntity {
    
    @Access(AccessType.PROPERTY)
    @OneToMany(targetEntity = StatusHistoricoDenuncia.class)
    private List<StatusHistoricoDenuncia> status;

    public Historico() {
    }

    public Historico(List<StatusHistoricoDenuncia> status) {
        this.status = status;
    }

    @ElementCollection
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
