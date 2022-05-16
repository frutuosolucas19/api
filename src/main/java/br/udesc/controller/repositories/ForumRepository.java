package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Forum;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ForumRepository implements PanacheRepository<Forum> {
    
}
