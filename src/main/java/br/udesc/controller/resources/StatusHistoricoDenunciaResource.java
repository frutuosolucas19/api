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

import br.udesc.controller.repositories.StatusHistoricoDenunciaRepository;
import br.udesc.model.StatusHistoricoDenuncia;

@Path("/statusHistoricoDenuncia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatusHistoricoDenunciaResource {

    @Inject
    StatusHistoricoDenunciaRepository statusHistoricoDenunciaRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<StatusHistoricoDenuncia> statusHistoricoDenuncia = statusHistoricoDenunciaRepository.listAll();
        return Response.ok(statusHistoricoDenuncia).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return statusHistoricoDenunciaRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(StatusHistoricoDenuncia statusHistoricoDenuncia) {
        statusHistoricoDenunciaRepository.persist(statusHistoricoDenuncia);
        if (statusHistoricoDenunciaRepository.isPersistent(statusHistoricoDenuncia)) {
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, StatusHistoricoDenuncia statusHistoricoDenuncia) {
        StatusHistoricoDenuncia s = statusHistoricoDenunciaRepository.findById(id);
        s.setStatus(statusHistoricoDenuncia.getStatus());
        s.setData(statusHistoricoDenuncia.getData());
        s.setHorario(statusHistoricoDenuncia.getHorario());
        return Response.status(200).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = statusHistoricoDenunciaRepository.deleteById(id);
        return deleted ? Response.noContent().
                build() : Response.status(404).build();
    }
    
}
