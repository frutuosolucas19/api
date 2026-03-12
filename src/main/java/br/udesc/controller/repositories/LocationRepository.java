package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import br.udesc.model.Location;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


@ApplicationScoped
public class LocationRepository implements PanacheRepository<Location>{
    
}


