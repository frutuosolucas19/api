package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import br.udesc.model.Place;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PlaceRepository implements PanacheRepository<Place>{
    
}


