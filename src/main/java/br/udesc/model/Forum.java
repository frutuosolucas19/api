package br.udesc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Forum extends PanacheEntity {

    @OneToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
    
    public Forum() {
    }

    public Forum(Usuario usuario) {
        this.usuario = usuario;
        
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
       
    @Override
    public String toString() {
        return "Forum{" +
                ", usuario=" + usuario +
                '}';
    }
}
