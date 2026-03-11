package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import br.udesc.model.Denuncia;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class DenunciaRepository implements PanacheRepository<Denuncia> {
    
    public List<Denuncia> findByUsuarioId(Long userId) {
        return list("usuario.id", userId);
    }
}

