package br.udesc.controller.resources;
import java.util.stream.Collectors;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import br.udesc.controller.repositories.UsuarioRepository;
import br.udesc.controller.security.JwtService;
import br.udesc.dto.LoginRequest;
import br.udesc.dto.LoginResponse;
import br.udesc.dto.UsuarioResponse;
import br.udesc.model.Usuario;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class UsuarioResource {

    @Inject UsuarioRepository usuarioRepository;
    @Inject JwtService jwtService;

    @GET
    public Response getAll() {
        var lista = usuarioRepository.listAll().stream()
            .map(u -> new UsuarioResponse(u.id, u.getPessoa(), u.getEmail(), u.getTipoUsuario()))
            .collect(Collectors.toList());

        return Response.ok(lista).build();
    }

    @GET
    @Path("/usuarios")
    public Response getAllLegacy() {
        return getAll();
    }

    @POST
    @Path("/cadastro")
    @PermitAll
    @Transactional
    public Response create(Usuario usuario) {
        if (usuario == null
            || usuario.getEmail() == null || usuario.getEmail().isBlank()
            || usuario.getPessoa() == null
            || usuario.getTipoUsuario() == null || usuario.getTipoUsuario().isBlank()
            || usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            return Response.status(400).entity("Dados obrigatorios ausentes").build();
        }

        usuario.setEmail(usuario.getEmail().trim().toLowerCase());
        if (usuarioRepository.count("email", usuario.getEmail()) > 0) {
            return Response.status(409).entity("E-mail ja cadastrado").build();
        }

        String hash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                .hashToString(12, usuario.getSenha().toCharArray());
        usuario.setSenhaHash(hash);
        usuario.setSenha(null); 

        usuarioRepository.persist(usuario);

        var resp = new br.udesc.dto.UsuarioResponse(usuario.id, usuario.getPessoa(),
                                                    usuario.getEmail(), usuario.getTipoUsuario());
        return Response.status(Response.Status.CREATED).entity(resp).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.email == null || loginRequest.senha == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email e senha sao obrigatorios.").build();
        }

        String emailLower = loginRequest.email.trim().toLowerCase();
        String senhaDigitada = loginRequest.senha;

        var usuarioOpt = usuarioRepository.findByEmail(emailLower);
        if (usuarioOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Email ou senha invalidos").build();
        }

        Usuario usuario = usuarioOpt.get();

        boolean ok = at.favre.lib.crypto.bcrypt.BCrypt.verifyer()
                .verify(senhaDigitada.toCharArray(), usuario.getSenhaHash())
                .verified;

        if (!ok) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Email ou senha invalidos").build();
        }

        String token = jwtService.gerarToken(usuario.getEmail(), usuario.id);

        LoginResponse resp = new LoginResponse(
                usuario.getPessoa() != null ? usuario.getPessoa().getNome() : null,
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                token
        );

        return Response.ok(resp).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return usuarioRepository.findByIdOptional(id)
            .map(u -> Response.ok(new br.udesc.dto.UsuarioResponse(u.id, u.getPessoa(), u.getEmail(), u.getTipoUsuario())).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = usuarioRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}

