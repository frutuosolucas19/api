package br.udesc.controller.resources;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.udesc.controller.repositories.PerguntaRepository;
import br.udesc.model.Pergunta;

@Path("/pergunta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PerguntaResource {

    @Inject
    PerguntaRepository perguntaRepository;

    @GET
    @Path("/perguntas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Pergunta> perguntas = perguntaRepository.listAll();
        return Response.ok(perguntas).build();
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
    public Response create(Pergunta pergunta) {
        perguntaRepository.persist(pergunta);
        if (perguntaRepository.isPersistent(pergunta)) {
            return Response.status(200).build();
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
        p.setForum(pergunta.getForum());
        p.setPergunta(pergunta.getPergunta());
        p.setRespostas(pergunta.getRespostas());
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
