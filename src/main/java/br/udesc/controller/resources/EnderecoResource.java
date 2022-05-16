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

import br.udesc.controller.repositories.EnderecoRepository;
import br.udesc.model.Endereco;

@Path("/endereco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {
    
    @Inject
    EnderecoRepository enderecoRepository;

    @GET
    @Path("/enderecos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Endereco> enderecos = enderecoRepository.listAll();
        return Response.ok(enderecos).build();
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
    public Response create(Endereco endereco) {
        enderecoRepository.persist(endereco);
        if (enderecoRepository.isPersistent(endereco)) {
            return Response.status(200).build();
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
        e.setCep(endereco.getCep());
        e.setLogradouro(endereco.getLogradouro());
        e.setNumero(endereco.getNumero());
        e.setComplemento(endereco.getComplemento());
        e.setBairro(endereco.getBairro());
        e.setLocalidade(endereco.getLocalidade());
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
