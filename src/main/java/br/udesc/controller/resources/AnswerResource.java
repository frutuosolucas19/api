package br.udesc.controller.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import br.udesc.controller.repositories.AnswerRepository;
import br.udesc.controller.repositories.QuestionRepository;
import br.udesc.dto.AnswerRequest;
import br.udesc.dto.ErrorResponse;
import br.udesc.model.Answer;
import br.udesc.model.Question;

@Path("/resposta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class AnswerResource {

    @Inject AnswerRepository respostaRepository;
    @Inject QuestionRepository perguntaRepository;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        List<Answer> respostas = respostaRepository.findAll().page(page, size).list();
        return Response.ok(respostas).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return respostaRepository.findByIdOptional(id)
                .map(r -> Response.ok(r).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response create(@Valid AnswerRequest req, @Context UriInfo uriInfo) {
        Question pergunta = perguntaRepository.findById(req.perguntaId);
        if (pergunta == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Pergunta nao encontrada para o perguntaId informado."))
                    .build();
        }
        Answer resposta = new Answer(pergunta, req.resposta.trim());
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
    public Response update(@PathParam("id") Long id, AnswerRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Answer r = respostaRepository.findById(id);
        if (r == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (req.perguntaId != null) {
            Question pergunta = perguntaRepository.findById(req.perguntaId);
            if (pergunta == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Pergunta nao encontrada para o perguntaId informado."))
                        .build();
            }
            r.setPergunta(pergunta);
        }
        if (req.resposta != null && !req.resposta.isBlank()) r.setResposta(req.resposta.trim());

        return Response.ok(r).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = respostaRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
