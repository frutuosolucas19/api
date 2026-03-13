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

import br.udesc.controller.repositories.AddressRepository;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Address> enderecos = enderecoRepository.listAll();
        return Response.ok(enderecos).build();
    }

    @GET
    @Path("/enderecos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLegacy() {
        return getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return enderecoRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Address endereco, @Context UriInfo uriInfo) {
        if (endereco == null
                || endereco.getLogradouro() == null || endereco.getLogradouro().isBlank()
                || endereco.getCidade() == null || endereco.getCidade().isBlank()
                || endereco.getUf() == null || endereco.getUf().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Campos obrigatorios: logradouro, cidade, uf."))
                    .build();
        }

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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Address endereco) {
        if (endereco == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Address e = enderecoRepository.findById(id);
        if (e == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (endereco.getCep() != null) e.setCep(endereco.getCep());
        if (endereco.getLogradouro() != null && !endereco.getLogradouro().isBlank()) e.setLogradouro(endereco.getLogradouro());
        if (endereco.getNumero() != null) e.setNumero(endereco.getNumero());
        if (endereco.getComplemento() != null) e.setComplemento(endereco.getComplemento());
        if (endereco.getBairro() != null) e.setBairro(endereco.getBairro());
        if (endereco.getCidade() != null && !endereco.getCidade().isBlank()) e.setCidade(endereco.getCidade());
        if (endereco.getUf() != null && !endereco.getUf().isBlank()) e.setUf(endereco.getUf());
        return Response.status(200).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = enderecoRepository.deleteById(id);
        return deleted ? Response.noContent().
                build() : Response.status(404).build();
    }
}



