package br.udesc.controller.resources;

import br.udesc.controller.repositories.ReportRepository;
import br.udesc.controller.repositories.UserRepository;
import br.udesc.dto.ReportRequest;
import br.udesc.dto.ReportResponse;
import br.udesc.model.*;
import io.quarkus.security.identity.SecurityIdentity;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Base64;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import java.util.stream.Collectors;

@Path("/denuncia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class ReportResource {

    @Inject ReportRepository denunciaRepository;
    @Inject UserRepository usuarioRepository;
    @Inject SecurityIdentity identity;

    @GET
    public Response getAll(@Context UriInfo uriInfo) {
        List<ReportResponse> lista = denunciaRepository.listAll().stream()
                .map(d -> toResponse(d, uriInfo))
                .collect(Collectors.toList());
        return Response.ok(lista).build();
    }

    @GET
    @Path("/denuncias")
    public Response getAllLegacy(@Context UriInfo uriInfo) {
        return getAll(uriInfo);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id, @Context UriInfo uriInfo) {
        User user = getUsuarioLogado();

        Report d = denunciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Report nao encontrada", 404));

        if (d.getUsuario() == null || !d.getUsuario().id.equals(user.id)) {
            throw new WebApplicationException("Acesso negado", 403);
        }

        return Response.ok(toResponse(d, uriInfo)).build();
    }

    @GET
    @Path("/minhas")
    public Response minhas(@Context UriInfo uriInfo) {
        User user = getUsuarioLogado();

        List<ReportResponse> lista = denunciaRepository.findByUsuarioId(user.id).stream()
                .map(d -> toResponse(d, uriInfo))
                .collect(Collectors.toList());

        return Response.ok(lista).build();
    }

    @POST
    @Transactional
    public Response create(ReportRequest req, @Context UriInfo uriInfo) {
        validarCamposObrigatorios(req);

        User user = getUsuarioLogado();

        Address e = fromEnderecoRequest(req);

        Report d = new Report();
        d.setNomeLocal(req.nomeLocal);
        d.setEndereco(e);
        d.setProblema(req.problema);
        d.setSugestao(req.sugestao);
        d.setUsuario(user);

        if (d.getStatus() == null) {
            d.setStatus(Status.ENCAMINHADO);
        }

        anexarImagensRequest(d, req);

        denunciaRepository.persist(d);

        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(d.id)).build();
        return Response.created(location).entity(toResponse(d, uriInfo)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, ReportRequest req, @Context UriInfo uriInfo) {
        User user = getUsuarioLogado();

        Report d = denunciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Report nao encontrada", 404));

        if (d.getUsuario() == null || !d.getUsuario().id.equals(user.id)) {
            throw new WebApplicationException("Acesso negado", 403);
        }

        if (req.nomeLocal != null && !req.nomeLocal.isBlank()) d.setNomeLocal(req.nomeLocal);
        if (req.problema  != null && !req.problema.isBlank()) d.setProblema(req.problema);
        if (req.sugestao  != null) d.setSugestao(req.sugestao);

        if (req.endereco != null) {
            if (d.getEndereco() == null) d.setEndereco(new Address());
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
        User user = getUsuarioLogado();

        Report d = denunciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Report nao encontrada", 404));

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
        User user = getUsuarioLogado();

        Report d = denunciaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Report nao encontrada", 404));

        if (d.getUsuario() == null || !d.getUsuario().id.equals(user.id)) {
            throw new WebApplicationException("Acesso negado", 403);
        }

        Image img = d.getImagens().stream()
                .filter(i -> i.id.equals(imgId))
                .findFirst()
                .orElseThrow(() -> new WebApplicationException("Image nao encontrada", 404));

        return Response.ok(new ByteArrayInputStream(img.getDados()))
                .type(img.getContentType() != null ? img.getContentType() : "application/octet-stream")
                .header("Content-Disposition", "inline; filename=\"" +
                        (img.getFilename() != null ? img.getFilename() : ("denuncia-" + imgId)) + "\"")
                .build();
    }

    private User getUsuarioLogado() {
        String email = identity.getPrincipal().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new WebApplicationException("User nao encontrado", 401));
    }

    private void validarCamposObrigatorios(ReportRequest req) {
        if (req == null
                || req.nomeLocal == null || req.nomeLocal.isBlank()
                || req.problema  == null || req.problema.isBlank()
                || req.endereco  == null
                || req.endereco.cidade == null || req.endereco.cidade.isBlank()
                || req.endereco.uf == null || req.endereco.uf.isBlank()) {
            throw new WebApplicationException("Campos obrigatorios: nomeLocal, problema, endereco.cidade, endereco.uf", 400);
        }
    }

    private Address fromEnderecoRequest(ReportRequest req) {
        Address e = new Address();
        aplicarEndereco(e, req);
        return e;
    }

    private void aplicarEndereco(Address e, ReportRequest req) {
        if (req.endereco.logradouro != null)  e.setLogradouro(req.endereco.logradouro);
        if (req.endereco.numero != null)      e.setNumero(req.endereco.numero);
        if (req.endereco.bairro != null)      e.setBairro(req.endereco.bairro);
        if (req.endereco.cidade != null)      e.setCidade(req.endereco.cidade);
        if (req.endereco.uf != null)          e.setUf(req.endereco.uf);
        if (req.endereco.cep != null)         e.setCep(req.endereco.cep);
        if (req.endereco.complemento != null) e.setComplemento(req.endereco.complemento);
    }

    private void anexarImagensRequest(Report d, ReportRequest req) {
        if (req.imagens == null || req.imagens.isEmpty()) return;
        int i = 0;
        for (var imgReq : req.imagens) {
            if (imgReq == null || imgReq.base64 == null || imgReq.base64.isBlank()) continue;

            byte[] bytes = Base64.getDecoder().decode(imgReq.base64);

            Image di = new Image();
            di.setDados(bytes);
            di.setContentType(imgReq.contentType != null ? imgReq.contentType : "image/jpeg");
            di.setFilename(imgReq.filename);
            di.setTamanhoBytes((long) bytes.length);
            di.setOrdem(imgReq.ordem != null ? imgReq.ordem : i++);

            d.addImagem(di);
        }
    }

    private ReportResponse toResponse(Report d, UriInfo uriInfo) {
        var er = new ReportResponse.EnderecoResponse();
        if (d.getEndereco() != null) {
            er.logradouro  = d.getEndereco().getLogradouro();
            er.numero      = d.getEndereco().getNumero();
            er.bairro      = d.getEndereco().getBairro();
            er.cidade      = d.getEndereco().getCidade();
            er.uf          = d.getEndereco().getUf();
            er.cep         = d.getEndereco().getCep();
            er.complemento = d.getEndereco().getComplemento();
        }

        List<ReportResponse.ImagemMeta> imagens = d.getImagens().stream().map(di -> {
            var m = new ReportResponse.ImagemMeta();
            m.id = di.id;
            m.filename = di.getFilename();
            m.contentType = di.getContentType();
            m.ordem = di.getOrdem();
            m.tamanhoBytes = di.getTamanhoBytes();

            UriBuilder b = uriInfo.getBaseUriBuilder()
                    .path(ReportResource.class)
                    .path(ReportResource.class, "download"); // /denuncia/{id}/imagens/{imgId}
            URI url = b.build(d.id, di.id);
            m.url = url.toString();
            return m;
        }).collect(Collectors.toList());

        String status = d.getStatus() != null ? d.getStatus().name() : null;

        return new ReportResponse(
                d.id,
                d.getNomeLocal(),
                er,
                d.getProblema(),
                d.getSugestao(),
                d.getUsuario() != null ? d.getUsuario().getEmail() : null,
                d.getCriadoEm(),
                status,
                imagens
        );
    }
}



