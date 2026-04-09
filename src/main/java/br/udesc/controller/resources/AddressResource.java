package br.udesc.controller.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import br.udesc.controller.repositories.AddressRepository;
import br.udesc.dto.AddressRequest;
import br.udesc.dto.ErrorResponse;
import br.udesc.model.Address;

@Path("/endereco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class AddressResource {

    @Inject
    AddressRepository enderecoRepository;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        List<Address> enderecos = enderecoRepository.findAll().page(page, size).list();
        return Response.ok(enderecos).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return enderecoRepository.findByIdOptional(id)
                .map(e -> Response.ok(e).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response create(@Valid AddressRequest req, @Context UriInfo uriInfo) {
        Address endereco = fromRequest(req);
        enderecoRepository.persist(endereco);
        if (enderecoRepository.isPersistent(endereco)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(endereco.id)).build();
            return Response.created(location).entity(endereco).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, AddressRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Address e = enderecoRepository.findById(id);
        if (e == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (req.logradouro != null && !req.logradouro.isBlank()) e.setLogradouro(req.logradouro);
        if (req.numero != null) e.setNumero(req.numero);
        if (req.bairro != null) e.setBairro(req.bairro);
        if (req.cidade != null && !req.cidade.isBlank()) e.setCidade(req.cidade);
        if (req.uf != null && !req.uf.isBlank()) e.setUf(req.uf);
        if (req.cep != null) e.setCep(req.cep);
        if (req.complemento != null) e.setComplemento(req.complemento);

        return Response.ok(e).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = enderecoRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    private Address fromRequest(AddressRequest req) {
        Address a = new Address();
        a.setLogradouro(req.logradouro);
        a.setNumero(req.numero);
        a.setBairro(req.bairro);
        a.setCidade(req.cidade);
        a.setUf(req.uf);
        a.setCep(req.cep);
        a.setComplemento(req.complemento);
        return a;
    }
}
