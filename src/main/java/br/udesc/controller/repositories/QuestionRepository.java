package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import br.udesc.model.Question;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class QuestionRepository implements PanacheRepository<Question>{
    
}


