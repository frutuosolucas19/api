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

import br.udesc.controller.repositories.LocationRepository;
import br.udesc.model.Location;

@Path("/localizacao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class LocationResource {

    @Inject
    LocationRepository localizacaoRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Location> localizacoes = localizacaoRepository.listAll();
        return Response.ok(localizacoes).build();
    }

    @GET
    @Path("/localizacoes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLegacy() {
        return getAll();
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
    public Response create(Location localizacao, @Context UriInfo uriInfo) {
        localizacaoRepository.persist(localizacao);
        if (localizacaoRepository.isPersistent(localizacao)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(localizacao.id)).build();
            return Response.created(location).entity(localizacao).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Location localizacao) {
        Location l = localizacaoRepository.findById(id);
        if (l == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
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



