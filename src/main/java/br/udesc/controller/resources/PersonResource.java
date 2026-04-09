package br.udesc.controller.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import br.udesc.controller.repositories.PersonRepository;
import br.udesc.dto.ErrorResponse;
import br.udesc.dto.PersonRequest;
import br.udesc.model.Person;

@Path("/pessoa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class PersonResource {

    @Inject
    PersonRepository pessoaRepository;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        List<Person> pessoas = pessoaRepository.findAll().page(page, size).list();
        return Response.ok(pessoas).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return pessoaRepository.findByIdOptional(id)
                .map(p -> Response.ok(p).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response create(@Valid PersonRequest req, @Context UriInfo uriInfo) {
        Person pessoa = new Person(req.nome.trim(),
                req.imagem != null ? req.imagem.trim() : null);
        pessoaRepository.persist(pessoa);
        if (pessoaRepository.isPersistent(pessoa)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(pessoa.id)).build();
            return Response.created(location).entity(pessoa).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, PersonRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Person p = pessoaRepository.findById(id);
        if (p == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (req.nome != null && !req.nome.isBlank()) p.setNome(req.nome.trim());
        if (req.imagem != null) p.setImagem(req.imagem.trim());

        return Response.ok(p).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = pessoaRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
