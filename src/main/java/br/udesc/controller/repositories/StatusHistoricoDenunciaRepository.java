package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.StatusHistoricoDenuncia;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class StatusHistoricoDenunciaRepository implements PanacheRepository<StatusHistoricoDenuncia> {
    
}