package br.udesc.controller.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import br.udesc.controller.repositories.PlaceRepository;
import br.udesc.dto.ErrorResponse;
import br.udesc.dto.PlaceRequest;
import br.udesc.model.Address;
import br.udesc.model.Location;
import br.udesc.model.Place;

@Path("/local")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class PlaceResource {

    @Inject
    PlaceRepository localRepository;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        List<Place> locais = localRepository.findAll().page(page, size).list();
        return Response.ok(locais).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return localRepository.findByIdOptional(id)
                .map(l -> Response.ok(l).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response create(@Valid PlaceRequest req, @Context UriInfo uriInfo) {
        Location localizacao = null;
        if (req.localizacao != null) {
            localizacao = new Location(req.localizacao.latitude.trim(), req.localizacao.longitude.trim());
        }

        Address endereco = null;
        if (req.endereco != null) {
            endereco = new Address();
            endereco.setLogradouro(req.endereco.logradouro);
            endereco.setNumero(req.endereco.numero);
            endereco.setBairro(req.endereco.bairro);
            endereco.setCidade(req.endereco.cidade);
            endereco.setUf(req.endereco.uf);
            endereco.setCep(req.endereco.cep);
            endereco.setComplemento(req.endereco.complemento);
        }

        Place local = new Place(req.nome.trim(), localizacao, endereco);
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
    public Response update(@PathParam("id") Long id, PlaceRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Payload obrigatorio."))
                    .build();
        }
        Place l = localRepository.findById(id);
        if (l == null) return Response.status(Response.Status.NOT_FOUND).build();

        if (req.nome != null && !req.nome.isBlank()) l.setNome(req.nome.trim());

        if (req.localizacao != null) {
            if (l.getLocalizacao() == null) l.setLocalizacao(new Location());
            if (req.localizacao.latitude != null && !req.localizacao.latitude.isBlank())
                l.getLocalizacao().setLatitude(req.localizacao.latitude.trim());
            if (req.localizacao.longitude != null && !req.localizacao.longitude.isBlank())
                l.getLocalizacao().setLongitude(req.localizacao.longitude.trim());
        }

        if (req.endereco != null) {
            if (l.getEndereco() == null) l.setEndereco(new Address());
            if (req.endereco.logradouro != null && !req.endereco.logradouro.isBlank())
                l.getEndereco().setLogradouro(req.endereco.logradouro);
            if (req.endereco.numero != null) l.getEndereco().setNumero(req.endereco.numero);
            if (req.endereco.bairro != null) l.getEndereco().setBairro(req.endereco.bairro);
            if (req.endereco.cidade != null && !req.endereco.cidade.isBlank())
                l.getEndereco().setCidade(req.endereco.cidade);
            if (req.endereco.uf != null && !req.endereco.uf.isBlank())
                l.getEndereco().setUf(req.endereco.uf);
            if (req.endereco.cep != null) l.getEndereco().setCep(req.endereco.cep);
            if (req.endereco.complemento != null) l.getEndereco().setComplemento(req.endereco.complemento);
        }

        return Response.ok(l).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = localRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
