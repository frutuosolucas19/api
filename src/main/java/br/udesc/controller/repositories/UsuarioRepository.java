package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import br.udesc.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Optional<Usuario> findByEmail(String emailLower) {
        if (emailLower == null) return Optional.empty();
        return find("email", emailLower).firstResultOptional();
    }

    public Optional<Usuario> findByEmailIgnoreCase(String emailAnyCase) {
        if (emailAnyCase == null) return Optional.empty();
        return find("lower(email) = ?1", emailAnyCase.trim().toLowerCase())
                .firstResultOptional();
    }

    public boolean existsByEmail(String emailLower) {
        if (emailLower == null) return false;
        return count("email", emailLower) > 0;
    }

}

