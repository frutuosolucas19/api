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

import br.udesc.controller.repositories.PlaceRepository;
import br.udesc.dto.ErrorResponse;
import br.udesc.model.Place;

@Path("/local")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class PlaceResource {

    @Inject
    PlaceRepository localRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Place> locais = localRepository.listAll();
        return Response.ok(locais).build();
    }

    @GET
    @Path("/pessoas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLegacy() {
        return getAll();
    }

    @GET
    @Path("/locais")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAlias() {
        return getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return localRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Place local, @Context UriInfo uriInfo) {
        if (local == null || local.getNome() == null || local.getNome().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Nome do local obrigatorio."))
                    .build();
        }
        localRepository.persist(local);
        if (localRepository.isPersistent(local)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(local.id)).build();
            return Response.created(location).entity(local).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Place local) {
        if (local == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Place l = localRepository.findById(id);
        if (l == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (local.getNome() != null && !local.getNome().isBlank()) l.setNome(local.getNome());
        if (local.getLocalizacao() != null) l.setLocalizacao(local.getLocalizacao());
        if (local.getEndereco() != null) l.setEndereco(local.getEndereco());
        return Response.status(200).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = localRepository.deleteById(id);
        return deleted ? Response.noContent().
                build() : Response.status(404).build();
    }
      
}



