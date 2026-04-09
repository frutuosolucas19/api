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
import br.udesc.controller.security.RateLimiterService;
import br.udesc.dto.ErrorResponse;
import br.udesc.dto.ForgotPasswordRequest;
import br.udesc.dto.ForgotPasswordResponse;
import br.udesc.dto.LoginRequest;
import br.udesc.dto.LoginResponse;
import br.udesc.dto.ResetPasswordRequest;
import br.udesc.dto.UserRequest;
import br.udesc.dto.UserResponse;
import br.udesc.model.Person;
import br.udesc.model.User;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class UserResource {

    private static final int BCRYPT_COST = 12;

    @Inject UserRepository usuarioRepository;
    @Inject JwtService jwtService;
    @Inject SecurityIdentity identity;
    @Inject RateLimiterService rateLimiter;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        User atual = getUsuarioAtual();
        if (atual == null || !"admin".equalsIgnoreCase(atual.getTipoUsuario())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse("Acesso restrito a administradores."))
                    .build();
        }
        var lista = usuarioRepository.findAll().page(page, size).list().stream()
                .map(u -> new UserResponse(u.id, u.getPessoa(), u.getEmail(), u.getTipoUsuario()))
                .collect(Collectors.toList());
        return Response.ok(lista).build();
    }

    @GET
    @Path("/me")
    public Response getMe() {
        User atual = getUsuarioAtual();
        if (atual == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(new UserResponse(atual.id, atual.getPessoa(), atual.getEmail(), atual.getTipoUsuario())).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        User atual = getUsuarioAtual();
        boolean isAdmin = atual != null && "admin".equalsIgnoreCase(atual.getTipoUsuario());
        boolean isSelf  = atual != null && atual.id.equals(id);
        if (!isAdmin && !isSelf) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse("Acesso negado."))
                    .build();
        }
        return usuarioRepository.findByIdOptional(id)
                .map(u -> Response.ok(new UserResponse(u.id, u.getPessoa(), u.getEmail(), u.getTipoUsuario())).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/cadastro")
    @PermitAll
    @Transactional
    public Response create(@Valid UserRequest req) {
        String email = req.email.trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("E-mail ja cadastrado."))
                    .build();
        }

        Person pessoa = new Person(req.pessoa.nome.trim(),
                req.pessoa.imagem != null ? req.pessoa.imagem.trim() : null);

        String hash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                .hashToString(BCRYPT_COST, req.senha.toCharArray());

        User usuario = new User(pessoa, email, hash, req.tipoUsuario.trim());
        usuarioRepository.persist(usuario);

        return Response.status(Response.Status.CREATED)
                .entity(new UserResponse(usuario.id, usuario.getPessoa(), usuario.getEmail(), usuario.getTipoUsuario()))
                .build();
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginRequest req) {
        String emailLower = req.email.trim().toLowerCase();
        if (!rateLimiter.isPermitido("login:" + emailLower)) {
            return Response.status(429)
                    .entity(new ErrorResponse("Muitas tentativas. Tente novamente em 1 minuto."))
                    .build();
        }

        var usuarioOpt = usuarioRepository.findByEmailIgnoreCase(emailLower);
        if (usuarioOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Email ou senha invalidos."))
                    .build();
        }

        User usuario = usuarioOpt.get();
        boolean ok = at.favre.lib.crypto.bcrypt.BCrypt.verifyer()
                .verify(req.senha.toCharArray(), usuario.getSenhaHash())
                .verified;

        if (!ok) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Email ou senha invalidos."))
                    .build();
        }

        String token = jwtService.gerarToken(usuario.getEmail(), usuario.id);
        LoginResponse resp = new LoginResponse(
                usuario.getPessoa() != null ? usuario.getPessoa().getNome() : null,
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                token);
        return Response.ok(resp).build();
    }

    @POST
    @Path("/esqueci-senha")
    @PermitAll
    public Response esqueciSenha(@Valid ForgotPasswordRequest request) {
        String email = request.email.trim().toLowerCase();
        if (!rateLimiter.isPermitido("esqueci-senha:" + email)) {
            return Response.status(429)
                    .entity(new ErrorResponse("Muitas tentativas. Tente novamente em 1 minuto."))
                    .build();
        }

        var usuarioOpt = usuarioRepository.findByEmailIgnoreCase(email);
        if (usuarioOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Usuario nao encontrado."))
                    .build();
        }

        String token = jwtService.gerarTokenRecuperacaoSenha(email);
        return Response.ok(new ForgotPasswordResponse(token)).build();
    }

    @POST
    @Path("/redefinir-senha")
    @PermitAll
    @Transactional
    public Response redefinirSenha(@Valid ResetPasswordRequest request) {
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
                .hashToString(BCRYPT_COST, request.novaSenha.toCharArray());
        usuario.setSenhaHash(novoHash);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, UserRequest payload) {
        if (payload == null) return badRequest("Payload obrigatorio.");

        User atual = getUsuarioAtual();
        boolean isAdmin = atual != null && "admin".equalsIgnoreCase(atual.getTipoUsuario());
        boolean isSelf  = atual != null && atual.id.equals(id);
        if (!isAdmin && !isSelf) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse("Acesso negado."))
                    .build();
        }

        User alvo = usuarioRepository.findById(id);
        if (alvo == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (payload.email != null && !payload.email.isBlank()) {
            String novoEmail = payload.email.trim().toLowerCase();
            var donoEmail = usuarioRepository.findByEmailIgnoreCase(novoEmail);
            if (donoEmail.isPresent() && !donoEmail.get().id.equals(alvo.id)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorResponse("E-mail ja cadastrado."))
                        .build();
            }
            alvo.setEmail(novoEmail);
        }

        if (payload.tipoUsuario != null && !payload.tipoUsuario.isBlank()) {
            alvo.setTipoUsuario(payload.tipoUsuario.trim());
        }

        if (payload.pessoa != null) {
            if (alvo.getPessoa() == null) alvo.setPessoa(new Person());
            if (payload.pessoa.nome != null && !payload.pessoa.nome.isBlank()) {
                alvo.getPessoa().setNome(payload.pessoa.nome.trim());
            }
            if (payload.pessoa.imagem != null) {
                alvo.getPessoa().setImagem(payload.pessoa.imagem.trim());
            }
        }

        if (payload.senha != null && !payload.senha.isBlank()) {
            if (payload.senha.trim().length() < 8) {
                return badRequest("Senha invalida. Minimo de 8 caracteres.");
            }
            String hash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                    .hashToString(BCRYPT_COST, payload.senha.toCharArray());
            alvo.setSenhaHash(hash);
        }

        return Response.ok(new UserResponse(alvo.id, alvo.getPessoa(), alvo.getEmail(), alvo.getTipoUsuario())).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        User atual = getUsuarioAtual();
        boolean isAdmin = atual != null && "admin".equalsIgnoreCase(atual.getTipoUsuario());
        boolean isSelf  = atual != null && atual.id.equals(id);
        if (!isAdmin && !isSelf) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse("Acesso negado."))
                    .build();
        }
        boolean deleted = usuarioRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    private User getUsuarioAtual() {
        var principal = identity.getPrincipal();
        if (principal == null || principal.getName() == null) return null;
        return usuarioRepository.findByEmailIgnoreCase(principal.getName()).orElse(null);
    }

    private Response badRequest(String mensagem) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(mensagem))
                .build();
    }
}
