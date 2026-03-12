package br.udesc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Forum extends PanacheEntity {

    @OneToOne
    @JoinColumn(name="usuario_id")
    private User usuario;
    
    public Forum() {
    }

    public Forum(User usuario) {
        this.usuario = usuario;
        
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
       
    @Override
    public String toString() {
        return "Forum{" +
                ", usuario=" + usuario +
                '}';
    }
}


