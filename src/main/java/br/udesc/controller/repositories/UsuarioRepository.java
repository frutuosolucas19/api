package br.udesc.controller.repositories;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import br.udesc.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    @Transactional
    public Usuario verificarCredenciais(String login, String senha) {
        return Usuario.find("login = ?1 and senha = ?2", login, senha).firstResult();
    }
}
