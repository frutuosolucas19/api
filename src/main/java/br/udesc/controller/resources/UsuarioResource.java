package br.udesc.controller.resources;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import br.udesc.controller.repositories.UsuarioRepository;
import br.udesc.dto.UsuarioResponse;
import br.udesc.model.Usuario;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioRepository usuarioRepository;

    //funciona
    @GET
    @Path("/usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Usuario> usuarios = usuarioRepository.listAll();
        return Response.ok(usuarios).build();
    }

    //funciona
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Usuario usuario) {
        usuarioRepository.persist(usuario);
        if (usuarioRepository.isPersistent(usuario)) {
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }

        @GET
        @Path("/{login}/{senha}")
        public Response login(@PathParam("login") String login, @PathParam("senha") String senha) {
            
        Usuario usuario = Usuario.find("login = ?1 and senha = ?2", login, senha).firstResult();
        
        if (usuario != null) {
            return Response.ok(usuario).build();  // Credenciais válidas
        }
        
        return Response.status(401).build();  // Status 401 Unauthorized
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return usuarioRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Usuario usuario) {
        Usuario u = usuarioRepository.findById(id);
        u.setTipoUsuario(usuario.getTipoUsuario());
        u.setLogin(usuario.getLogin());
        u.setSenha(usuario.getSenha());
        u.setPessoa(usuario.getPessoa());
        return Response.status(200).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = usuarioRepository.deleteById(id);
        return deleted ? Response.noContent().
                build() : Response.status(404).build();
    }
}
