package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import br.udesc.model.Person;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

}



