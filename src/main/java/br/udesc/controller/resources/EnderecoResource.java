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

import br.udesc.controller.repositories.EnderecoRepository;
import br.udesc.model.Endereco;

@Path("/endereco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class EnderecoResource {
    
    @Inject
    EnderecoRepository enderecoRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Endereco> enderecos = enderecoRepository.listAll();
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
    public Response create(Endereco endereco, @Context UriInfo uriInfo) {
        enderecoRepository.persist(endereco);
        if (enderecoRepository.isPersistent(endereco)) {
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(endereco.id)).build();
            return Response.created(location).entity(endereco).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Endereco endereco) {
        Endereco e = enderecoRepository.findById(id);
        if (e == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        e.setCep(endereco.getCep());
        e.setLogradouro(endereco.getLogradouro());
        e.setNumero(endereco.getNumero());
        e.setComplemento(endereco.getComplemento());
        e.setBairro(endereco.getBairro());
        e.setCidade(endereco.getCidade());
        e.setUf(endereco.getUf());
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


