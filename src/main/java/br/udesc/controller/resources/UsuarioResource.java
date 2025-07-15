package br.udesc.controller.resources;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.udesc.controller.repositories.UsuarioRepository;
import br.udesc.dto.LoginRequest;
import br.udesc.dto.LoginResponse;
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
            return Response.status(200).entity(usuario).build(); // retorna o objeto como JSON
        }
        return Response.status(400).entity("Erro ao persistir usuário").build();
    }

        @GET
        @Path("/{login}/{senha}")
        public Response login(@PathParam("login") String login, @PathParam("senha") String senha) {
            
        Usuario usuario = Usuario.find("login = ?1 and senha = ?2", login, senha).firstResult();
        
        if (usuario != null) {
            return Response.ok(usuario).build();
        }
        
        return Response.status(401).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(
            loginRequest.email, loginRequest.senha
        );

        if (usuario == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Email ou senha inválidos").build();
        }

        String nome = usuario.getPessoa().getNome(); 
        String tipo = usuario.getTipoUsuario();
        String email = usuario.getEmail();

        return Response.ok(new LoginResponse(nome, email, tipo)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return usuarioRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
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
