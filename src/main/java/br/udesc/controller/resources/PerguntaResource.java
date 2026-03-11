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

import br.udesc.controller.repositories.PerguntaRepository;
import br.udesc.model.Pergunta;

@Path("/pergunta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class PerguntaResource {

    @Inject
    PerguntaRepository perguntaRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Pergunta> perguntas = perguntaRepository.listAll();
        return Response.ok(perguntas).build();
    }

    @GET
    @Path("/perguntas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLegacy() {
        return getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return perguntaRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Pergunta pergunta, @Context UriInfo uriInfo) {
        perguntaRepository.persist(pergunta);
        if (perguntaRepository.isPersistent(pergunta)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(pergunta.id)).build();
            return Response.created(location).entity(pergunta).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Pergunta pergunta) {
        Pergunta p = perguntaRepository.findById(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        p.setForum(pergunta.getForum());
        p.setPergunta(pergunta.getPergunta());
        return Response.status(200).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = perguntaRepository.deleteById(id);
        return deleted ? Response.noContent().
                build() : Response.status(404).build();
    }
    
}


