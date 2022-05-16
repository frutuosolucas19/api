package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Local;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class LocalRepository implements PanacheRepository<Local>{
    
}
