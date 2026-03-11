package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import br.udesc.model.Status;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class StatusRepository implements PanacheRepository<Status> {
    
}

