package br.udesc.controller.resources;
import java.util.stream.Collectors;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import br.udesc.controller.repositories.UserRepository;
import br.udesc.controller.security.JwtService;
import br.udesc.dto.ErrorResponse;
import br.udesc.dto.ForgotPasswordRequest;
import br.udesc.dto.LoginRequest;
import br.udesc.dto.LoginResponse;
import br.udesc.dto.ResetPasswordRequest;
import br.udesc.dto.UserResponse;
import br.udesc.model.User;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class UserResource {
    private static final int TAMANHO_MINIMO_SENHA = 8;
    private static final int BCRYPT_COST = 12;

    @Inject UserRepository usuarioRepository;
    @Inject JwtService jwtService;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        var lista = usuarioRepository.findAll().page(page, size).list().stream()
            .map(u -> new UserResponse(u.id, u.getPessoa(), u.getEmail(), u.getTipoUsuario()))
            .collect(Collectors.toList());

        return Response.ok(lista).build();
    }

    @GET
    @Path("/usuarios")
    public Response getAllLegacy(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        return getAll(page, size);
    }

    @POST
    @Path("/cadastro")
    @PermitAll
    @Transactional
    public Response create(User usuario) {
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
        if (!isSenhaValida(usuario.getSenha())) {
            return badRequest("Senha invalida. Minimo de 8 caracteres.");
        }

        String hash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                .hashToString(BCRYPT_COST,usuario.getSenha().toCharArray());
        usuario.setSenhaHash(hash);
        usuario.setSenha(null); 

        usuarioRepository.persist(usuario);

        var resp = new br.udesc.dto.UserResponse(usuario.id, usuario.getPessoa(),
                                                    usuario.getEmail(), usuario.getTipoUsuario());
        return Response.status(Response.Status.CREATED).entity(resp).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.email == null || loginRequest.senha == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email e senha sao obrigatorios.").build();
        }

        String emailLower = loginRequest.email.trim().toLowerCase();
        String senhaDigitada = loginRequest.senha;

        var usuarioOpt = usuarioRepository.findByEmail(emailLower);
        if (usuarioOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Email ou senha invalidos").build();
        }

        User usuario = usuarioOpt.get();

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

    @POST
    @Path("/esqueci-senha")
    @PermitAll
    public Response esqueciSenha(@Valid ForgotPasswordRequest request) {
        if (request == null || request.email == null || request.email.isBlank()) {
            return badRequest("Email obrigatorio.");
        }

        String email = request.email.trim().toLowerCase();
        if (!isEmailValido(email)) {
            return badRequest("Email invalido.");
        }

        var usuarioOpt = usuarioRepository.findByEmailIgnoreCase(email);
        if (usuarioOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("User nao encontrado."))
                    .build();
        }

        // Token gerado para integracao com servico de e-mail externo.
        jwtService.gerarTokenRecuperacaoSenha(email);
        return Response.noContent().build();
    }

    @POST
    @Path("/redefinir-senha")
    @PermitAll
    @Transactional
    public Response redefinirSenha(@Valid ResetPasswordRequest request) {
        if (request == null
                || request.tokenAws == null || request.tokenAws.isBlank()
                || request.novaSenha == null || request.novaSenha.isBlank()) {
            return badRequest("tokenAws e novaSenha sao obrigatorios.");
        }

        if (!isSenhaValida(request.novaSenha)) {
            return badRequest("Senha invalida. Minimo de 8 caracteres.");
        }

        String email = jwtService.validarEExtrairEmailTokenRecuperacaoSenha(request.tokenAws.trim());
        if (email == null) {
            return badRequest("Token invalido ou expirado.");
        }

        var usuarioOpt = usuarioRepository.findByEmailIgnoreCase(email);
        if (usuarioOpt.isEmpty()) {
            return badRequest("Token invalido ou expirado.");
        }

        User usuario = usuarioOpt.get();
        String novoHash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                .hashToString(BCRYPT_COST,request.novaSenha.toCharArray());
        usuario.setSenhaHash(novoHash);
        usuarioRepository.persist(usuario);

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return usuarioRepository.findByIdOptional(id)
            .map(u -> Response.ok(new br.udesc.dto.UserResponse(u.id, u.getPessoa(), u.getEmail(), u.getTipoUsuario())).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, User payload) {
        if (payload == null) {
            return badRequest("Payload obrigatorio.");
        }

        User atual = usuarioRepository.findById(id);
        if (atual == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (payload.getEmail() != null && !payload.getEmail().isBlank()) {
            String novoEmail = payload.getEmail().trim().toLowerCase();
            var donoEmail = usuarioRepository.findByEmailIgnoreCase(novoEmail);
            if (donoEmail.isPresent() && !donoEmail.get().id.equals(atual.id)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorResponse("E-mail ja cadastrado."))
                        .build();
            }
            atual.setEmail(novoEmail);
        }

        if (payload.getPessoa() != null) {
            atual.setPessoa(payload.getPessoa());
        }

        if (payload.getTipoUsuario() != null && !payload.getTipoUsuario().isBlank()) {
            atual.setTipoUsuario(payload.getTipoUsuario().trim());
        }

        if (payload.getSenha() != null && !payload.getSenha().isBlank()) {
            if (!isSenhaValida(payload.getSenha())) {
                return badRequest("Senha invalida. Minimo de 8 caracteres.");
            }
            String hash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                    .hashToString(BCRYPT_COST,payload.getSenha().toCharArray());
            atual.setSenhaHash(hash);
        }

        return Response.ok(new UserResponse(atual.id, atual.getPessoa(), atual.getEmail(), atual.getTipoUsuario())).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = usuarioRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    private Response badRequest(String mensagem) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(mensagem))
                .build();
    }

    private boolean isEmailValido(String email) {
        int at = email.indexOf('@');
        int dot = email.lastIndexOf('.');
        return at > 0 && dot > at + 1 && dot < email.length() - 1;
    }

    private boolean isSenhaValida(String senha) {
        return senha != null && senha.trim().length() >= TAMANHO_MINIMO_SENHA;
    }
}


