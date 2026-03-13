package br.udesc.controller.resources;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

import br.udesc.controller.repositories.AnswerRepository;
import br.udesc.dto.ErrorResponse;
import br.udesc.model.Answer;

@Path("/resposta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class AnswerResource {

    @Inject
    AnswerRepository respostaRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Answer> respostas = respostaRepository.listAll();
        return Response.ok(respostas).build();
    }

    @GET
    @Path("/respostas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLegacy() {
        return getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return respostaRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Answer resposta, @Context UriInfo uriInfo) {
        if (resposta == null
                || resposta.getResposta() == null || resposta.getResposta().isBlank()
                || resposta.getPergunta() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Campos obrigatorios: resposta, pergunta."))
                    .build();
        }
        respostaRepository.persist(resposta);
        if (respostaRepository.isPersistent(resposta)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(resposta.id)).build();
            return Response.created(location).entity(resposta).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Answer resposta) {
        if (resposta == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Answer r = respostaRepository.findById(id);
        if (r == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (resposta.getPergunta() != null) r.setPergunta(resposta.getPergunta());
        if (resposta.getResposta() != null && !resposta.getResposta().isBlank()) r.setResposta(resposta.getResposta());
        return Response.status(200).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = respostaRepository.deleteById(id);
        return deleted ? Response.noContent().
                build() : Response.status(404).build();
    }
    
}



