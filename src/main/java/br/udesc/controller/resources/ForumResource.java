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
import br.udesc.controller.repositories.UserRepository;
import br.udesc.dto.ErrorResponse;
import br.udesc.dto.ForumRequest;
import br.udesc.model.Forum;
import br.udesc.model.User;

@Path("/forum")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class ForumResource {

    @Inject ForumRepository forumRepository;
    @Inject UserRepository usuarioRepository;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        List<Forum> foruns = forumRepository.findAll().page(page, size).list();
        return Response.ok(foruns).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return forumRepository.findByIdOptional(id)
                .map(f -> Response.ok(f).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response create(@Valid ForumRequest req, @Context UriInfo uriInfo) {
        User usuario = usuarioRepository.findById(req.usuarioId);
        if (usuario == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Usuario nao encontrado para o usuarioId informado."))
                    .build();
        }
        Forum forum = new Forum(usuario);
        forumRepository.persist(forum);
        if (forumRepository.isPersistent(forum)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(forum.id)).build();
            return Response.created(location).entity(forum).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, ForumRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Forum f = forumRepository.findById(id);
        if (f == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (req.usuarioId != null) {
            User usuario = usuarioRepository.findById(req.usuarioId);
            if (usuario == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Usuario nao encontrado para o usuarioId informado."))
                        .build();
            }
            f.setUsuario(usuario);
        }
        return Response.ok(f).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = forumRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
