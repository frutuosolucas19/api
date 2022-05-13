package br.udesc.controller;

import javax.ws.rs.Path;

import br.udesc.model.Pessoa;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;

@Path("/pessoa")
public interface PessoaResource extends PanacheEntityResource<Pessoa, Integer> {

   
   
}