package br.udesc.controller;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Pessoa;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PessoaRepository implements PanacheRepository<Pessoa> {

}

