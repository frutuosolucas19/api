package br.udesc.controller.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import br.udesc.controller.repositories.LocationRepository;
import br.udesc.dto.ErrorResponse;
import br.udesc.dto.LocationRequest;
import br.udesc.model.Location;

@Path("/localizacao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class LocationResource {

    @Inject
    LocationRepository localizacaoRepository;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        List<Location> localizacoes = localizacaoRepository.findAll().page(page, size).list();
        return Response.ok(localizacoes).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return localizacaoRepository.findByIdOptional(id)
                .map(l -> Response.ok(l).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response create(@Valid LocationRequest req, @Context UriInfo uriInfo) {
        Location localizacao = new Location(req.latitude.trim(), req.longitude.trim());
        localizacaoRepository.persist(localizacao);
        if (localizacaoRepository.isPersistent(localizacao)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(localizacao.id)).build();
            return Response.created(location).entity(localizacao).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, LocationRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Location l = localizacaoRepository.findById(id);
        if (l == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (req.latitude != null && !req.latitude.isBlank()) l.setLatitude(req.latitude.trim());
        if (req.longitude != null && !req.longitude.isBlank()) l.setLongitude(req.longitude.trim());

        return Response.ok(l).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = localizacaoRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
