package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import br.udesc.model.Address;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {
    
}


