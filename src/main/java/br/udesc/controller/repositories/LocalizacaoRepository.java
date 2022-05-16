package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Localizacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


@ApplicationScoped
public class LocalizacaoRepository implements PanacheRepository<Localizacao>{
    
}
