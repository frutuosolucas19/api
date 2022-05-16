package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Historico;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class HistoricoRepository implements PanacheRepository<Historico>{
    
}
