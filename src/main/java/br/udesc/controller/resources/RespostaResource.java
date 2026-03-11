package br.udesc.controller.resources;

import java.util.List;

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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import br.udesc.controller.repositories.RespostaRepository;
import br.udesc.model.Resposta;

@Path("/resposta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RespostaResource {

    @Inject
    RespostaRepository respostaRepository;

    @GET
    @Path("/respostas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Resposta> respostas = respostaRepository.listAll();
        return Response.ok(respostas).build();
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
    public Response create(Resposta resposta) {
        respostaRepository.persist(resposta);
        if (respostaRepository.isPersistent(resposta)) {
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Resposta resposta) {
        Resposta r = respostaRepository.findById(id);
        r.setPergunta(resposta.getPergunta());
        r.setResposta(resposta.getResposta());
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

