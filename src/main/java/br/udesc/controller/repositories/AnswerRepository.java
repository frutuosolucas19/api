package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import br.udesc.model.Answer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class AnswerRepository implements PanacheRepository<Answer>{
    
}


