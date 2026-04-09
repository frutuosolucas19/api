package br.udesc.controller.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import br.udesc.controller.repositories.ForumRepository;
import br.udesc.controller.repositories.QuestionRepository;
import br.udesc.dto.ErrorResponse;
import br.udesc.dto.QuestionRequest;
import br.udesc.model.Forum;
import br.udesc.model.Question;

@Path("/pergunta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class QuestionResource {

    @Inject QuestionRepository perguntaRepository;
    @Inject ForumRepository forumRepository;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        List<Question> perguntas = perguntaRepository.findAll().page(page, size).list();
        return Response.ok(perguntas).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return perguntaRepository.findByIdOptional(id)
                .map(p -> Response.ok(p).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response create(@Valid QuestionRequest req, @Context UriInfo uriInfo) {
        Forum forum = forumRepository.findById(req.forumId);
        if (forum == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Forum nao encontrado para o forumId informado."))
                    .build();
        }
        Question pergunta = new Question(forum, req.pergunta.trim());
        perguntaRepository.persist(pergunta);
        if (perguntaRepository.isPersistent(pergunta)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(pergunta.id)).build();
            return Response.created(location).entity(pergunta).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, QuestionRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Question p = perguntaRepository.findById(id);
        if (p == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (req.forumId != null) {
            Forum forum = forumRepository.findById(req.forumId);
            if (forum == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Forum nao encontrado para o forumId informado."))
                        .build();
            }
            p.setForum(forum);
        }
        if (req.pergunta != null && !req.pergunta.isBlank()) p.setPergunta(req.pergunta.trim());

        return Response.ok(p).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = perguntaRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
