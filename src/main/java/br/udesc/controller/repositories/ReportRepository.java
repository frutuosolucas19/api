package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import br.udesc.model.Report;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ReportRepository implements PanacheRepository<Report> {
    
    public List<Report> findByUsuarioId(Long userId) {
        return list("usuario.id", userId);
    }
}


