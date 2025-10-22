package br.udesc.controller.resources;

import br.udesc.controller.repositories.DenunciaRepository;
import br.udesc.controller.repositories.UsuarioRepository;
import br.udesc.dto.DenunciaRequest;
import br.udesc.dto.DenunciaResponse;
import br.udesc.model.Denuncia;
import br.udesc.model.Imagem;
import br.udesc.model.Endereco;
import br.udesc.model.Usuario;
import io.quarkus.security.identity.SecurityIdentity;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Path("/denuncia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DenunciaResource {

    @Inject DenunciaRepository denunciaRepository;
    @Inject UsuarioRepository usuarioRepository;
    @Inject SecurityIdentity identity;

    @GET
    @Path("/denuncias")
    public Response getAll(@Context UriInfo uriInfo) {
        List<DenunciaResponse> lista = denunciaRepository.listAll().stream()
                .map(d -> toResponse(d, uriInfo))
                .collect(Collectors.toList());
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id, @Context UriInfo uriInfo) {
        Usuario user = getUsuarioLogado();

        Denuncia d = denunciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Denúncia não encontrada", 404));

        if (d.getUsuario() == null || !d.getUsuario().id.equals(user.id)) {
            throw new WebApplicationException("Acesso negado", 403);
        }

        return Response.ok(toResponse(d, uriInfo)).build();
    }

    @GET
    @Path("/minhas")
    public Response minhas(@Context UriInfo uriInfo) {
        Usuario user = getUsuarioLogado();

        List<DenunciaResponse> lista = denunciaRepository.findByUsuarioId(user.id).stream()
                .map(d -> toResponse(d, uriInfo))
                .collect(Collectors.toList());

        return Response.ok(lista).build();
    }

    @POST
    @Transactional
    public Response create(DenunciaRequest req, @Context UriInfo uriInfo) {
        validarCamposObrigatorios(req);

        Usuario user = getUsuarioLogado();

        Endereco e = fromEnderecoRequest(req);

        Denuncia d = new Denuncia();
        d.setNomeLocal(req.nomeLocal);
        d.setEndereco(e);
        d.setProblema(req.problema);
        d.setSugestao(req.sugestao);
        d.setUsuario(user);

        anexarImagensRequest(d, req);

        denunciaRepository.persist(d);

        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(d.id)).build();
        return Response.created(location).entity(toResponse(d, uriInfo)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, DenunciaRequest req, @Context UriInfo uriInfo) {
        Usuario user = getUsuarioLogado();

        Denuncia d = denunciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Denúncia não encontrada", 404));

        if (d.getUsuario() == null || !d.getUsuario().id.equals(user.id)) {
            throw new WebApplicationException("Acesso negado", 403);
        }

        if (req.nomeLocal != null && !req.nomeLocal.isBlank()) d.setNomeLocal(req.nomeLocal);
        if (req.problema  != null && !req.problema.isBlank()) d.setProblema(req.problema);
        if (req.sugestao  != null) d.setSugestao(req.sugestao);

        if (req.endereco != null) {
            if (d.getEndereco() == null) d.setEndereco(new Endereco());
            aplicarEndereco(d.getEndereco(), req);
        }

        if (req.imagens != null) {
            d.getImagens().clear();
            anexarImagensRequest(d, req);
        }

        return Response.ok(toResponse(d, uriInfo)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        Usuario user = getUsuarioLogado();

        Denuncia d = denunciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Denúncia não encontrada", 404));

        if (d.getUsuario() == null || !d.getUsuario().id.equals(user.id)) {
            throw new WebApplicationException("Acesso negado", 403);
        }

        denunciaRepository.delete(d);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/imagens/{imgId}")
    @Produces({ "image/jpeg", "image/png", "image/webp", "application/octet-stream" })
    public Response download(@PathParam("id") Long id,
                             @PathParam("imgId") Long imgId) {
        Usuario user = getUsuarioLogado();

        Denuncia d = denunciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Denúncia não encontrada", 404));

        if (d.getUsuario() == null || !d.getUsuario().id.equals(user.id)) {
            throw new WebApplicationException("Acesso negado", 403);
        }

        Imagem img = d.getImagens().stream()
                .filter(i -> i.id.equals(imgId))
                .findFirst()
                .orElseThrow(() -> new WebApplicationException("Imagem não encontrada", 404));

        return Response.ok(new ByteArrayInputStream(img.getDados()))
                .type(img.getContentType() != null ? img.getContentType() : "application/octet-stream")
                .header("Content-Disposition", "inline; filename=\"" +
                        (img.getFilename() != null ? img.getFilename() : ("denuncia-" + imgId)) + "\"")
                .build();
    }


    private Usuario getUsuarioLogado() {
        String email = identity.getPrincipal().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new WebApplicationException("Usuário não encontrado", 401));
    }

    private void validarCamposObrigatorios(DenunciaRequest req) {
        if (req == null
                || req.nomeLocal == null || req.nomeLocal.isBlank()
                || req.problema  == null || req.problema.isBlank()
                || req.endereco  == null
                || req.endereco.cidade == null || req.endereco.cidade.isBlank()
                || req.endereco.uf == null || req.endereco.uf.isBlank()) {
            throw new WebApplicationException("Campos obrigatórios: nomeLocal, problema, endereco.cidade, endereco.uf", 400);
        }
    }

    private Endereco fromEnderecoRequest(DenunciaRequest req) {
        Endereco e = new Endereco();
        aplicarEndereco(e, req);
        return e;
    }

    private void aplicarEndereco(Endereco e, DenunciaRequest req) {
        if (req.endereco.logradouro != null) e.setLogradouro(req.endereco.logradouro);
        if (req.endereco.numero != null)     e.setNumero(req.endereco.numero);
        if (req.endereco.bairro != null)     e.setBairro(req.endereco.bairro);
        if (req.endereco.cidade != null)     e.setCidade(req.endereco.cidade);
        if (req.endereco.uf != null)         e.setUf(req.endereco.uf);
        if (req.endereco.cep != null)        e.setCep(req.endereco.cep);
        if (req.endereco.complemento != null)e.setComplemento(req.endereco.complemento);
    }

    private void anexarImagensRequest(Denuncia d, DenunciaRequest req) {
        if (req.imagens == null || req.imagens.isEmpty()) return;
        int i = 0;
        for (var imgReq : req.imagens) {
            if (imgReq == null || imgReq.base64 == null || imgReq.base64.isBlank()) continue;

            byte[] bytes = Base64.getDecoder().decode(imgReq.base64);

            Imagem di = new Imagem();
            di.setDados(bytes);
            di.setContentType(imgReq.contentType != null ? imgReq.contentType : "image/jpeg");
            di.setFilename(imgReq.filename);
            di.setTamanhoBytes((long) bytes.length);
            di.setOrdem(imgReq.ordem != null ? imgReq.ordem : i++);

            d.addImagem(di);
        }
    }

    private DenunciaResponse toResponse(Denuncia d, UriInfo uriInfo) {
        var er = new DenunciaResponse.EnderecoResponse();
        if (d.getEndereco() != null) {
            er.logradouro  = d.getEndereco().getLogradouro();
            er.numero      = d.getEndereco().getNumero();
            er.bairro      = d.getEndereco().getBairro();
            er.cidade      = d.getEndereco().getCidade();
            er.uf          = d.getEndereco().getUf();
            er.cep         = d.getEndereco().getCep();
            er.complemento = d.getEndereco().getComplemento();
        }

        List<DenunciaResponse.ImagemMeta> imagens = d.getImagens().stream().map(di -> {
            var m = new DenunciaResponse.ImagemMeta();
            m.id = di.id;
            m.filename = di.getFilename();
            m.contentType = di.getContentType();
            m.ordem = di.getOrdem();
            m.tamanhoBytes = di.getTamanhoBytes();

            UriBuilder b = uriInfo.getBaseUriBuilder()
                    .path(DenunciaResource.class)
                    .path(DenunciaResource.class, "download");
            URI url = b.build(d.id, di.id);
            m.url = url.toString();
            return m;
        }).collect(Collectors.toList());

        return new DenunciaResponse(
                d.id,
                d.getNomeLocal(),
                er,
                d.getProblema(),
                d.getSugestao(),
                d.getUsuario() != null ? d.getUsuario().getEmail() : null,
                d.getCriadoEm(),
                imagens
        );
    }
}
