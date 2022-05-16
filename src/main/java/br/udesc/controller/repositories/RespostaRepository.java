package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Resposta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class RespostaRepository implements PanacheRepository<Resposta>{
    
}
