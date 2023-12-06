package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.udesc.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {
    
    public Usuario findByUsernameAndPassword(String login, String senha) {
        return find("login = ?1 and senha = ?2", login, senha).firstResult();
    }

    public Usuario findByUsername(String login) {
        return find("login", login).firstResult();
    }
}
