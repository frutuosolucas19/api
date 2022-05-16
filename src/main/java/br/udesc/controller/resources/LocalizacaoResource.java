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

import br.udesc.controller.repositories.LocalizacaoRepository;
import br.udesc.model.Localizacao;

@Path("/localizacao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocalizacaoResource {

    @Inject
    LocalizacaoRepository localizacaoRepository;

    @GET
    @Path("/localizacoes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Localizacao> localizacoes = localizacaoRepository.listAll();
        return Response.ok(localizacoes).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return localizacaoRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Localizacao localizacao) {
        localizacaoRepository.persist(localizacao);
        if (localizacaoRepository.isPersistent(localizacao)) {
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Localizacao localizacao) {
        Localizacao l = localizacaoRepository.findById(id);
        l.setLatitude(localizacao.getLatitude());
        l.setLongitude(localizacao.getLongitude());
        return Response.status(200).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = localizacaoRepository.deleteById(id);
        return deleted ? Response.noContent().
                build() : Response.status(404).build();
    }
    
}
