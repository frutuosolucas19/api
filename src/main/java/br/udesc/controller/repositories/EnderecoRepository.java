package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Endereco;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class EnderecoRepository implements PanacheRepository<Endereco> {
    
}
