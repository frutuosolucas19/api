package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Pergunta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PerguntaRepository implements PanacheRepository<Pergunta>{
    
}
