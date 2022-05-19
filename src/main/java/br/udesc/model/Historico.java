package br.udesc.model;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Historico extends PanacheEntity {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="historico_id")
    private List<StatusHistoricoDenuncia> statusHistoricoDenuncias;

    public Historico() {
    }

    public Historico(List<StatusHistoricoDenuncia> statusHistoricoDenuncias) {
        this.statusHistoricoDenuncias = statusHistoricoDenuncias;
    }

    @ElementCollection
    public List<StatusHistoricoDenuncia> getstatusHistoricoDenuncias() {
        return statusHistoricoDenuncias;
    }

    public void setStatusHistoricoDenuncias(List<StatusHistoricoDenuncia> statusHistoricoDenuncias) {
        this.statusHistoricoDenuncias = statusHistoricoDenuncias;
    }

    @Override
    public String toString() {
        return "Historico{" +
                "id=" + id +
                ", statusHistoricoDenuncias=" + statusHistoricoDenuncias +
                '}';
    }
}
