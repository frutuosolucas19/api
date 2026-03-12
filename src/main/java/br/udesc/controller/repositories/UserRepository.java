package br.udesc.controller.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import br.udesc.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByEmail(String emailLower) {
        if (emailLower == null) return Optional.empty();
        return find("email", emailLower).firstResultOptional();
    }

    public Optional<User> findByEmailIgnoreCase(String emailAnyCase) {
        if (emailAnyCase == null) return Optional.empty();
        return find("lower(email) = ?1", emailAnyCase.trim().toLowerCase())
                .firstResultOptional();
    }

    public boolean existsByEmail(String emailLower) {
        if (emailLower == null) return false;
        return count("email", emailLower) > 0;
    }

}


